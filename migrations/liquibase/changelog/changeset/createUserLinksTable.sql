CREATE TABLE users_links
(
    userId BIGINT NOT NULL,
    linkId BIGINT NOT NULL,
    PRIMARY KEY (userId, linkId)
);

ALTER TABLE users_links
    ADD CONSTRAINT fk_relUser_userId
        FOREIGN KEY (userId)
            REFERENCES users (id);

ALTER TABLE users_links
    ADD CONSTRAINT fk_relLink_linkId
        FOREIGN KEY (linkId)
            REFERENCES links (id);
