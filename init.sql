CREATE INDEX idx_artifact_in_storage ON artifact_storage (uuid, artifact_uuid);


CREATE INDEX idx_magical_properties ON magical_property (danger_level);

CREATE INDEX idx_accounts_user ON deposit_account (owner_passportid, money_type, balance DESC);

create function get_account_transactions_multiple_types(acc_id uuid, transaction_types text[])
    returns TABLE(moneytype text, accountid uuid, transactionamount numeric, transactiontype text)
    language plpgsql
as
$$
BEGIN
    RETURN QUERY
        SELECT
            ac.money_type::TEXT AS moneyType, -- Приведение к TEXT
            tr.to_account_id AS accountId,
            tr.amount AS transactionAmount,
            tr.transaction_type::TEXT AS transactionType -- Приведение к TEXT
        FROM
            deposit_account ac
                LEFT JOIN
            transaction tr
            ON
                ac.id = tr.from_account_id
        WHERE
            ac.id = acc_id
          AND tr.transaction_type = ANY(transaction_types);
END;
$$;

alter function get_account_transactions_multiple_types(uuid, text[]) owner to "user";


CREATE OR REPLACE FUNCTION get_filtered_artifacts(
    some_owner TEXT DEFAULT NULL,
    some_magic_properties TEXT[] DEFAULT NULL
)
    RETURNS TABLE(
                     artifact_id UUID,
                     artifact_name VARCHAR,
                     created_date TIMESTAMP WITHOUT TIME ZONE,
                     owner_passport_id VARCHAR,
                     magical_danger_level VARCHAR,
                     last_change_date TIMESTAMP WITHOUT TIME ZONE,
                     last_reason_to_save VARCHAR
                 )
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        SELECT
            a.uuid AS artifact_id,
            a.name AS artifact_name,
            a.created_at AS created_date,
            c.passportid AS owner_passport_id,
            mp.danger_level AS magical_danger_level,
            ah.change_date AS last_change_date,
            ah.reason_to_save AS last_reason_to_save
        FROM artifact a
                 LEFT JOIN client c ON a.current_client_passport_id = c.passportid
                 LEFT JOIN magical_property mp ON a.magical_property_uuid = mp.uuid
                 LEFT JOIN artifact_history ah ON a.history_uuid = ah.uuid
        WHERE (c.passportid = some_owner OR some_owner IS NULL)
          AND (some_magic_properties IS NULL OR mp.danger_level = ANY(some_magic_properties)) -- Измененная проверка
        ORDER BY a.created_at DESC;
END;
$$;

ALTER FUNCTION get_filtered_artifacts(TEXT, TEXT[]) OWNER TO "user";



CREATE OR REPLACE FUNCTION transfer_deposit_accounts(
    from_account_id UUID,
    to_account_id UUID,
    transfer_amount NUMERIC,
    OUT transfer_status TEXT
)
    RETURNS TEXT LANGUAGE plpgsql AS $$
DECLARE
    from_balance NUMERIC;
    to_balance NUMERIC;
    from_money_type TEXT;
    to_money_type TEXT;
BEGIN
    SELECT balance, money_type INTO from_balance, from_money_type
    FROM deposit_account
    WHERE id = from_account_id;

    IF from_balance IS NULL THEN
        transfer_status := 'FAILED';
        RETURN;
    END IF;

    SELECT balance, money_type INTO to_balance, to_money_type
    FROM deposit_account
    WHERE id = to_account_id;

    IF to_balance IS NULL THEN
        transfer_status := 'FAILED';
        RETURN;
    END IF;

    IF from_money_type != to_money_type THEN
        transfer_status := 'FAILED';
        RETURN;
    END IF;

    IF from_balance < transfer_amount THEN
        transfer_status := 'FAILED';
        RETURN;
    END IF;

    UPDATE deposit_account
    SET balance = balance - transfer_amount
    WHERE id = from_account_id;

    UPDATE deposit_account
    SET balance = balance + transfer_amount
    WHERE id = to_account_id;

    transfer_status := 'SUCCEEDED';
    RETURN;
END;
$$;