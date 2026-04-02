package com.debanjan.spring_security.services;

import com.debanjan.spring_security.dtos.DecodedUser;
import com.debanjan.spring_security.dtos.UserLoginRequest;
import com.debanjan.spring_security.dtos.UserResponseDTO;
import com.debanjan.spring_security.dtos.UserSignUpRequest;
import com.debanjan.spring_security.entities.UserEntity;
import com.debanjan.spring_security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JWTService jwtService;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponseDTO signUpUser(UserSignUpRequest request){
        UserEntity userEntity = userService.signUpUser(request);
        String token = jwtService.generateJWT(userEntity);

        return UserResponseDTO.builder()
                .email(userEntity.getEmail())
                .fullName(userEntity.getFullName())
                .token(token)
                .id(userEntity.getId())
                .build();
    }

    public UserResponseDTO authenticateUser(UserLoginRequest request){

        Authentication auth = null;

        try{
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        }catch (Exception e){
            throw new BadCredentialsException("Invalid credentials");
        }

        User user = auth.getPrincipal() instanceof User ? (User) auth.getPrincipal() : null;

        if(user == null){
            throw new BadCredentialsException("Provided credentials are not valid.");
        }

        UserEntity userEntity = userService.getUserByEmail(user.getUsername());

        String token = jwtService.generateJWT(userEntity);

        return UserResponseDTO.builder()
                .email(userEntity.getEmail())
                .fullName(userEntity.getFullName())
                .token(token)
                .id(userEntity.getId())
                .build();
    }

    public DecodedUser getUserDetails(String token){
        if(token == null || token.isBlank()){
            throw new BadCredentialsException("Invalid or missing token");
        }

        return jwtService.getUserDetails(token);
    }
}
