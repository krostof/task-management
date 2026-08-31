--liquibase formatted sql

--changeset dev:004-create-project-member-table
CREATE TABLE project_members
(
    id            UUID        NOT NULL,
    version       BIGINT,
    creation_date TIMESTAMP WITHOUT TIME ZONE,
    update_date   TIMESTAMP WITHOUT TIME ZONE,
    created_by    VARCHAR(255),
    user_id       UUID        NOT NULL,
    project_id    UUID        NOT NULL,
    role          VARCHAR(20) NOT NULL,
    CONSTRAINT pk_project_members PRIMARY KEY (id)
);

ALTER TABLE project_members
    ADD CONSTRAINT FK_PROJECT_MEMBERS_ON_PROJECT FOREIGN KEY (project_id) REFERENCES projects (id);

ALTER TABLE project_members
    ADD CONSTRAINT FK_PROJECT_MEMBERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);