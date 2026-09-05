package com.example.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    UUID id;

    @Version
    private Long version;

    @CreatedDate
    LocalDateTime creationDate;

    @LastModifiedDate
    LocalDateTime updateDate;

    String createdBy;

}
