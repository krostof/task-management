package com.example.domain.service;

import com.example.domain.dto.UserDto;
import com.example.domain.exception.EmailAlreadyExistException;
import com.example.domain.repository.UserRepository;
import com.example.domain.entity.User;
import com.example.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User save(UserDto user) {

        if (userRepository.existsByEmail(user.email())) {
            throw new EmailAlreadyExistException("Email already exists: " +  user.email());
        }

        User userEntity = mapUserDtoToUser(user);

        return userRepository.save(userEntity);
    }

    private User mapUserDtoToUser(UserDto user) {
        User userEntity = new User();
        userEntity.setName(user.name());
        userEntity.setSurname(user.surname());
        userEntity.setEmail(user.email());
        userEntity.setPassword(user.password());
        userEntity.setRole(UserRole.USER);
        return userEntity;
    }

}
