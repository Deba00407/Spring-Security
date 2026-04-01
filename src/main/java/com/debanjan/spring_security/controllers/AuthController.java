package com.debanjan.spring_security.controllers;

import com.debanjan.spring_security.dtos.DecodedUser;
import com.debanjan.spring_security.dtos.UserLoginRequest;
import com.debanjan.spring_security.dtos.UserSignUpRequest;
import com.debanjan.spring_security.services.AuthService;
import com.debanjan.spring_security.utils.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    // function to extract jwt token from request token
    private String extractAuthToken(String token){
        if(token == null || token.isBlank() || !token.startsWith("Bearer")){
            throw new AuthenticationCredentialsNotFoundException("Invalid or missing token");
        }

        return token.substring(7);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Validated UserSignUpRequest userSignUpRequest, HttpServletRequest request){
        String token = authService.signUpUser(userSignUpRequest);

        SuccessResponse<Map<String, String>> response = SuccessResponse.<Map<String, String>>builder().message("User signed up successfully").data(Map.of("token", token))
                .status(HttpStatus.CREATED.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserLoginRequest userLoginRequest, HttpServletRequest request){
        String token = authService.authenticateUser(userLoginRequest);

        SuccessResponse<Map<String, String>> response = SuccessResponse.<Map<String, String>>builder().message("User signed up successfully").data(Map.of("token", token))
                .status(HttpStatus.OK.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token, HttpServletRequest request){
        String authToken = extractAuthToken(token);

        DecodedUser user = authService.getUserDetails(authToken);

        SuccessResponse<DecodedUser> response = SuccessResponse.<DecodedUser>builder()
                        .data(user)
                        .path(request.getRequestURI())
                        .message("User details fetched successfully")
                        .status(HttpStatus.OK.value())
                        .build();

        return ResponseEntity.ok(response);
    }
}
