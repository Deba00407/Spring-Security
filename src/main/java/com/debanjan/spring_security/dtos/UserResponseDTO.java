package com.debanjan.spring_security.dtos;

import lombok.Builder;

@Builder
public record UserResponseDTO(
        Long id,
        String fullName,
        String email,
        String token
) {
}
