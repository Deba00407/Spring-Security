package com.debanjan.spring_security.services;

import com.debanjan.spring_security.dtos.DecodedUser;
import com.debanjan.spring_security.dtos.UserLoginRequest;
import com.debanjan.spring_security.dtos.UserSignUpRequest;
import com.debanjan.spring_security.entities.User;
import com.debanjan.spring_security.exceptions.ConflictException;
import com.debanjan.spring_security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Transactional
    public String signUpUser(UserSignUpRequest request){
        Optional<User> existingUser = userRepository.findByEmail(request.email());
        if(existingUser.isPresent()){
            throw new ConflictException("User with the email address already exists");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);

        return jwtService.generateJWT(user);
    }

    public String authenticateUser(UserLoginRequest request){
        Optional<User> user = userRepository.findByEmail(request.email());
        if(user.isPresent() && passwordEncoder.matches(request.password(), user.get().getPassword())){
            return jwtService.generateJWT(user.get());
        }

        return null;
    }

    public DecodedUser getUserDetails(String token){
        if(token == null || token.isBlank()){
            throw new BadCredentialsException("Invalid or missing token");
        }

        return jwtService.getUserDetails(token);
    }
}
