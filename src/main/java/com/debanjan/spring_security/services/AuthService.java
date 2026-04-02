package com.debanjan.spring_security.services;

import com.debanjan.spring_security.dtos.DecodedUser;
import com.debanjan.spring_security.dtos.UserLoginRequest;
import com.debanjan.spring_security.dtos.UserResponseDTO;
import com.debanjan.spring_security.dtos.UserSignUpRequest;
import com.debanjan.spring_security.entities.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JWTService jwtService;

    private final UserService userService;

    @Transactional
    public UserResponseDTO signUpUser(UserSignUpRequest request){
        User user = userService.signUpUser(request);
        String token = jwtService.generateJWT(user);

        return UserResponseDTO.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .token(token)
                .id(user.getId())
                .build();
    }

    public UserResponseDTO authenticateUser(UserLoginRequest request){
        User user = userService.loginUser(request);
        if(user == null){
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateJWT(user);

        return UserResponseDTO.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .token(token)
                .id(user.getId())
                .build();
    }

    public DecodedUser getUserDetails(String token){
        if(token == null || token.isBlank()){
            throw new BadCredentialsException("Invalid or missing token");
        }

        return jwtService.getUserDetails(token);
    }
}
