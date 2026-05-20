--liquibase formatted sql

--changeset dev:001-create-users-table
CREATE TABLE users
(
    id            UUID         NOT NULL,
    version       BIGINT,
    creation_date TIMESTAMP WITHOUT TIME ZONE,
    update_date   TIMESTAMP WITHOUT TIME ZONE,
    created_by    VARCHAR(255),
    name          VARCHAR(50)  NOT NULL,
    surname       VARCHAR(100) NOT NULL,
    password      VARCHAR(60)  NOT NULL,
    email         VARCHAR(100) NOT NULL,
    role          VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);