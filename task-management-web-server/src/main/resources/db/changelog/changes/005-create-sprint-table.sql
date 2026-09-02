--liquibase formatted sql

--changeset dev:005-create-sprint-table
CREATE TABLE sprints
(
    id            UUID         NOT NULL,
    version       BIGINT,
    creation_date TIMESTAMP WITHOUT TIME ZONE,
    update_date   TIMESTAMP WITHOUT TIME ZONE,
    created_by    VARCHAR(255),
    name          VARCHAR(100) NOT NULL,
    start_date    date         NOT NULL,
    end_date      date         NOT NULL,
    sprint_status VARCHAR(20)  NOT NULL,
    project_id    UUID         NOT NULL,
    CONSTRAINT pk_sprints PRIMARY KEY (id)
);

ALTER TABLE sprints
    ADD CONSTRAINT FK_SPRINTS_ON_PROJECT FOREIGN KEY (project_id) REFERENCES projects (id);