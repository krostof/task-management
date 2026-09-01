package com.example.domain.entity;


import com.example.domain.BaseEntity;
import com.example.domain.enums.SprintStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "sprints")
public class Sprint extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SprintStatus sprintStatus;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
