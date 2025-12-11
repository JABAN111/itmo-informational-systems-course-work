-- Initial database schema migration

-- Create tables

CREATE TABLE IF NOT EXISTS client (
    passportid VARCHAR(255) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS staff (
    staff_name VARCHAR(255) PRIMARY KEY,
    role VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS deposit_account (
    id UUID PRIMARY KEY,
    owner_passportid VARCHAR(255) NOT NULL,
    money_type VARCHAR(50) NOT NULL,
    balance NUMERIC(19, 2) NOT NULL,
    deposit_account_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (owner_passportid) REFERENCES client(passportid)
);

CREATE TABLE IF NOT EXISTS transaction (
    uuid UUID PRIMARY KEY,
    from_account_id UUID NOT NULL,
    to_account_id UUID,
    amount NUMERIC(19, 2),
    transaction_status VARCHAR(50),
    transaction_type VARCHAR(50),
    transaction_date TIMESTAMP,
    transaction_staff_name VARCHAR(255),
    FOREIGN KEY (from_account_id) REFERENCES deposit_account(id),
    FOREIGN KEY (to_account_id) REFERENCES deposit_account(id),
    FOREIGN KEY (transaction_staff_name) REFERENCES staff(staff_name)
);

CREATE TABLE IF NOT EXISTS magical_property (
    uuid UUID PRIMARY KEY,
    danger_level VARCHAR(255) NOT NULL,
    property VARCHAR(1000) NOT NULL
);

CREATE TABLE IF NOT EXISTS artifact (
    name VARCHAR(255) PRIMARY KEY,
    magical_property_uuid UUID NOT NULL,
    current_client_passport_id VARCHAR(255),
    is_stored BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (magical_property_uuid) REFERENCES magical_property(uuid),
    FOREIGN KEY (current_client_passport_id) REFERENCES client(passportid)
);

CREATE TABLE IF NOT EXISTS artifact_storage (
    uuid UUID PRIMARY KEY,
    artifact_name VARCHAR(255),
    FOREIGN KEY (artifact_name) REFERENCES artifact(name)
);

CREATE TABLE IF NOT EXISTS key (
    uuid UUID PRIMARY KEY,
    artifact_storage_uuid UUID,
    client_passport_id VARCHAR(255),
    jwt_token TEXT NOT NULL,
    issued_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    giver_staff_name VARCHAR(255),
    FOREIGN KEY (artifact_storage_uuid) REFERENCES artifact_storage(uuid),
    FOREIGN KEY (client_passport_id) REFERENCES client(passportid),
    FOREIGN KEY (giver_staff_name) REFERENCES staff(staff_name)
);

CREATE TABLE IF NOT EXISTS artifact_history (
    uuid UUID PRIMARY KEY,
    artifact_name VARCHAR(255),
    change_date TIMESTAMP NOT NULL,
    reason_to_save VARCHAR(1000) NOT NULL,
    FOREIGN KEY (artifact_name) REFERENCES artifact(name)
);

CREATE TABLE IF NOT EXISTS artifact_history_clients (
    artifact_history_id UUID NOT NULL,
    client_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (artifact_history_id, client_id),
    FOREIGN KEY (artifact_history_id) REFERENCES artifact_history(uuid),
    FOREIGN KEY (client_id) REFERENCES client(passportid)
);

-- Create indexes

CREATE INDEX IF NOT EXISTS idx_artifact_in_storage ON artifact_storage (uuid, artifact_name);

CREATE INDEX IF NOT EXISTS idx_magical_properties ON magical_property (danger_level);

CREATE INDEX IF NOT EXISTS idx_accounts_user ON deposit_account (owner_passportid, money_type, balance DESC);

-- Create functions

CREATE OR REPLACE FUNCTION get_account_transactions_multiple_types(acc_id uuid, transaction_types text[])
    RETURNS TABLE(moneytype text, accountid uuid, transactionamount numeric, transactiontype text)
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY
        SELECT
            ac.money_type::TEXT AS moneyType,
            tr.to_account_id AS accountId,
            tr.amount AS transactionAmount,
            tr.transaction_type::TEXT AS transactionType
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

CREATE OR REPLACE FUNCTION get_filtered_artifacts(
    some_owner TEXT DEFAULT NULL,
    some_magic_properties VARCHAR[] DEFAULT NULL
)
    RETURNS TABLE(
                     artifact_nam VARCHAR,
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
            a.name AS artifact_nam,
            a.created_at AS created_date,
            c.passportid AS owner_passport_id,
            mp.danger_level AS magical_danger_level,
            ah.change_date AS last_change_date,
            ah.reason_to_save AS last_reason_to_save

        FROM artifact_history ah
            LEFT JOIN artifact a on ah.artifact_name = a.name
            LEFT JOIN client c ON a.current_client_passport_id = c.passportid
            LEFT JOIN magical_property mp ON a.magical_property_uuid = mp.uuid


        WHERE (c.passportid = some_owner OR some_owner IS NULL)
          AND (some_magic_properties IS NULL OR mp.danger_level like ANY(some_magic_properties))
        ORDER BY a.created_at DESC;
END;
$$;

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
