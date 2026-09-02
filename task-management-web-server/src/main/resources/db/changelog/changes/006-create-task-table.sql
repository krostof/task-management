--liquibase formatted sql

--changeset dev:006-create-task-table
CREATE TABLE tasks
(
    id            UUID          NOT NULL,
    version       BIGINT,
    creation_date TIMESTAMP WITHOUT TIME ZONE,
    update_date   TIMESTAMP WITHOUT TIME ZONE,
    created_by    VARCHAR(255),
    title         VARCHAR(200)  NOT NULL,
    description   VARCHAR(2000) NOT NULL,
    task_status   VARCHAR(20)   NOT NULL,
    task_priority VARCHAR(20)   NOT NULL,
    sprint_id     UUID,
    project_id    UUID          NOT NULL,
    creator_id    UUID          NOT NULL,
    assignee_id   UUID,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_ASSIGNEE FOREIGN KEY (assignee_id) REFERENCES users (id);

ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id);

ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_PROJECT FOREIGN KEY (project_id) REFERENCES projects (id);

ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_SPRINT FOREIGN KEY (sprint_id) REFERENCES sprints (id);