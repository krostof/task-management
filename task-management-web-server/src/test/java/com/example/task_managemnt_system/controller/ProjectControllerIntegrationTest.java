package com.example.task_managemnt_system.controller;

import com.example.api.model.AuthResponse;
import com.example.api.model.ProjectRequest;
import com.example.api.model.RegisterRequest;
import com.example.domain.entity.Project;
import com.example.domain.enums.ProjectStatus;
import com.example.domain.repository.ProjectRepository;
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
public class ProjectControllerIntegrationTest {

    private static final String CREATE_PROJECT_PATH = "/api/v1/projects";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void createProject_withValidRequestAndAuthentication_persistsProjectAndReturnsIt() throws Exception {
        String email = "email@test.ts";
        String token = registerAndGetToken(email);

        ProjectRequest projectRequest = new ProjectRequest()
                .name("Proj Name")
                .description("Example of description");

        String responseBody = mockMvc.perform(post(CREATE_PROJECT_PATH)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Proj Name"))
                .andExpect(jsonPath("$.projectStatus").value("ACTIVE"))
                .andReturn().getResponse().getContentAsString();

        UUID projectId = UUID.fromString(objectMapper.readTree(responseBody).get("id").asText());
        Optional<Project> persisted = projectRepository.findById(projectId);
        assertThat(persisted).isPresent();
        assertThat(persisted.get().getProjectStatus()).isEqualTo(ProjectStatus.ACTIVE);
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
}
