package com.debanjan.spring_security.services;

import com.debanjan.spring_security.dtos.UserResponseDTO;
import com.debanjan.spring_security.entities.UserEntity;
import com.debanjan.spring_security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    private UserResponseDTO mapToUserResponseDTO(UserEntity userEntity){
        return UserResponseDTO.builder()
                .id(userEntity.getId())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .build();
    }

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponseDTO)
                .toList();
    }
}
