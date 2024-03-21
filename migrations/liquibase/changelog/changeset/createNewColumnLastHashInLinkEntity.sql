ALTER TABLE links
    ADD COLUMN lastSendTime timestamptz DEFAULT current_timestamp;

