--liquibase formatted sql

--changeset dev:003-create-project-table
CREATE TABLE projects
(
    id            UUID         NOT NULL,
    version       BIGINT,
    creation_date TIMESTAMP WITHOUT TIME ZONE,
    update_date   TIMESTAMP WITHOUT TIME ZONE,
    created_by    VARCHAR(255),
    name          VARCHAR(100) NOT NULL,
    description   VARCHAR(255),
    CONSTRAINT pk_projects PRIMARY KEY (id)
);