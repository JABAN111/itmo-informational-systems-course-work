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