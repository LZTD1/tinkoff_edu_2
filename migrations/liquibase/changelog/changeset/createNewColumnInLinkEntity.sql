ALTER TABLE links
    ADD COLUMN updatetime timestamptz DEFAULT CURRENT_TIMESTAMP;

