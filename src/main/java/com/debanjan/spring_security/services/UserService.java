package com.debanjan.spring_security.services;

import com.debanjan.spring_security.dtos.UserSignUpRequest;
import com.debanjan.spring_security.entities.UserEntity;
import com.debanjan.spring_security.exceptions.ConflictException;
import com.debanjan.spring_security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity signUpUser(UserSignUpRequest signUpRequest){
        // verify if present already in the db
        userRepository.findByEmail(signUpRequest.email()).ifPresent(user -> {
            throw new ConflictException("User with the email address: %s already exists".formatted(user.getEmail()));
        });

        UserEntity userEntity = UserEntity.builder()
                .fullName(signUpRequest.fullName())
                .email(signUpRequest.email())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .build();

        userRepository.save(userEntity);

        return userEntity;
    }

    public UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }
}
