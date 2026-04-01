package com.debanjan.spring_security.utils;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponse <T>{

    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    private String message;
    private int status;
    private String path;
    private T data;
}
