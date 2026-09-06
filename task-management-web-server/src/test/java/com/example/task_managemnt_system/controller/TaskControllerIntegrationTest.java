package com.example.task_managemnt_system.controller;

import com.example.api.model.AuthResponse;
import com.example.api.model.RegisterRequest;
import com.example.api.model.TaskRequest;
import com.example.domain.entity.Project;
import com.example.domain.entity.Task;
import com.example.domain.enums.ProjectStatus;
import com.example.domain.enums.TaskStatus;
import com.example.domain.repository.ProjectRepository;
import com.example.domain.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIntegrationTest {

    private static final String CREATE_TASK_PATH = "/api/v1/tasks";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void createTask_withValidRequestAndAuthentication_persistsTaskAndReturnsIt() throws Exception {
        String email = "task-owner-" + System.nanoTime() + "@example.com";
        String token = registerAndGetToken(email);
        UUID projectId = saveProject().getId();

        TaskRequest taskRequest = new TaskRequest()
                .title("Implement login screen")
                .description("Build the UI for the login screen")
                .projectId(projectId)
                .taskPriority(TaskRequest.TaskPriorityEnum.HIGH);

        String responseBody = mockMvc.perform(post(CREATE_TASK_PATH)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Implement login screen"))
                .andExpect(jsonPath("$.taskPriority").value("HIGH"))
                .andExpect(jsonPath("$.taskStatus").value("NEW"))
                .andExpect(jsonPath("$.projectId").value(projectId.toString()))
                .andReturn().getResponse().getContentAsString();

        UUID taskId = UUID.fromString(objectMapper.readTree(responseBody).get("id").asText());
        Optional<Task> persisted = taskRepository.findById(taskId);
        assertThat(persisted).isPresent();
        assertThat(persisted.get().getTaskStatus()).isEqualTo(TaskStatus.NEW);
        assertThat(persisted.get().getCreator().getEmail()).isEqualTo(email);
        assertThat(persisted.get().getProject().getId()).isEqualTo(projectId);
    }

    @Test
    void createTask_withoutAuthorizationHeader_returnsUnauthorized() throws Exception {
        UUID projectId = saveProject().getId();

        TaskRequest taskRequest = new TaskRequest()
                .title("Task without auth")
                .projectId(projectId)
                .taskPriority(TaskRequest.TaskPriorityEnum.LOW);

        mockMvc.perform(post(CREATE_TASK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isForbidden());
    }

    private String registerAndGetToken(String email) throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Jan");
        registerRequest.setSurname("Kowalski");
        registerRequest.setEmail(email);
        registerRequest.setPassword("pass123");

        String responseBody = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseBody, AuthResponse.class).getToken();
    }

    private Project saveProject() {
        Project project = new Project();
        project.setName("Test project " + UUID.randomUUID());
        project.setDescription("Project created for integration tests");
        project.setProjectStatus(ProjectStatus.ACTIVE);
        return projectRepository.save(project);
    }
}
