package com.example.task_managemnt_system.controller;

import com.example.api.ProjectsApi;
import com.example.api.model.ProjectRequest;
import com.example.api.model.ProjectResponse;
import com.example.domain.entity.Project;
import com.example.domain.entity.User;
import com.example.domain.enums.ProjectStatus;
import com.example.domain.repository.ProjectRepository;
import com.example.task_managemnt_system.mapper.ProjectMapper;
import com.example.task_managemnt_system.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectsApi {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    @Override
    public ResponseEntity<ProjectResponse> createProject(ProjectRequest projectRequest) {

        Project project = projectMapper.toEntity(projectRequest);
        project.setCreatedBy(currentUser().getCreatedBy());
        project.setProjectStatus(ProjectStatus.ACTIVE);

        projectRepository.save(project);

        return ResponseEntity.status(HttpStatus.CREATED).body(projectMapper.toResponse(project));
    }

    private User currentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUser();
    }

}
