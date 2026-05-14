package com.example.domain.user;

import com.example.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, length = 50)
    String name;
    @Column(nullable = false, length = 100)
    String surname;
    @Column(nullable = false, length = 60)
    String password;
    @Column(nullable = false, unique = true, length = 100)
    String email;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    UserRole role;
}
