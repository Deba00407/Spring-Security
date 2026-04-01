package com.debanjan.spring_security.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignUpRequest (
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters.")
        String fullName,

        @Email(message = "Please enter a valid email.")
        String email,

        @Pattern(
                regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                message = "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one number, and one special character"
        )
        String password
){
}
