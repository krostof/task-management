package com.example.task_managemnt_system.controller;

import com.example.api.TasksApi;
import com.example.api.model.TaskRequest;
import com.example.api.model.TaskResponse;
import com.example.domain.entity.Project;
import com.example.domain.entity.Sprint;
import com.example.domain.entity.Task;
import com.example.domain.entity.User;
import com.example.domain.enums.TaskStatus;
import com.example.domain.repository.ProjectRepository;
import com.example.domain.repository.SprintRepository;
import com.example.domain.repository.TaskRepository;
import com.example.domain.repository.UserRepository;
import com.example.task_managemnt_system.mapper.TaskMapper;
import com.example.task_managemnt_system.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController implements TasksApi {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public ResponseEntity<TaskResponse> createTask(TaskRequest taskRequest) {
        if (taskRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        Task task = taskMapper.toEntity(taskRequest);
        task.setTaskStatus(TaskStatus.NEW);
        task.setCreator(currentUser());

        Task savedTask = taskRepository.save(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toResponse(savedTask));
    }

    private User currentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUser();
    }
}
