-- Undo migration for V1__Initial_schema.sql
-- Note: Undo migrations require Flyway Teams or Enterprise edition
-- This file is provided for reference and manual rollback if needed

-- Drop functions
DROP FUNCTION IF EXISTS transfer_deposit_accounts(UUID, UUID, NUMERIC);
DROP FUNCTION IF EXISTS get_filtered_artifacts(TEXT, VARCHAR[]);
DROP FUNCTION IF EXISTS get_account_transactions_multiple_types(UUID, TEXT[]);

-- Drop indexes
DROP INDEX IF EXISTS idx_accounts_user;
DROP INDEX IF EXISTS idx_magical_properties;
DROP INDEX IF EXISTS idx_artifact_in_storage;

-- Drop tables (in reverse order due to foreign keys)
DROP TABLE IF EXISTS artifact_history_clients;
DROP TABLE IF EXISTS artifact_history;
DROP TABLE IF EXISTS key;
DROP TABLE IF EXISTS artifact_storage;
DROP TABLE IF EXISTS artifact;
DROP TABLE IF EXISTS magical_property;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS deposit_account;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS client;
