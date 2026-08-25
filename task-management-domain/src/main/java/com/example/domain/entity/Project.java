package com.example.domain.entity;

import com.example.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "projects")
public class Project extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;
    @Column(length = 255)
    private String description;
    @OneToMany(mappedBy = "project")
    private List<ProjectMember> members;

}
