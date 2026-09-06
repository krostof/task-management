package com.example.task_managemnt_system.mapper;

import com.example.api.model.ProjectRequest;
import com.example.api.model.ProjectResponse;
import com.example.domain.entity.Project;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "projectStatus", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "sprints", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project toEntity(ProjectRequest projectRequest);

    ProjectResponse toResponse(Project project);
}
