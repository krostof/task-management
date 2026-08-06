package com.example.domain.dto;

public record UserDto(
        String name,
        String surname,
        String password,
        String email
) {
}
