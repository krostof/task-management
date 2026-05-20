--liquibase formatted sql

--changeset dev:002-create-users-email-unique
ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);