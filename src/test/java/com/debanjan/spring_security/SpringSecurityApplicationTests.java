package com.debanjan.spring_security;

import com.debanjan.spring_security.dtos.DecodedUser;
import com.debanjan.spring_security.dtos.UserSignUpRequest;
import com.debanjan.spring_security.entities.User;
import com.debanjan.spring_security.exceptions.ConflictException;
import com.debanjan.spring_security.services.AuthService;
import com.debanjan.spring_security.services.JWTService;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SpringSecurityApplicationTests {

	@Autowired
	private JWTService jwtService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthService authService;

	@Test
	void testPasswordEncoding(){
		String encodedPassword = passwordEncoder.encode("password");
		Assertions.assertFalse(passwordEncoder.matches("wrong_password", encodedPassword));
	}

	@Test
	void testUserDecoding() {
		User user = User.builder()
				.id(13L)
				.fullName("TestUser1")
				.email("TestUser1@example.com")
				.build();

		String token = jwtService.generateJWT(user);
		System.out.println("Token: " + token);

		DecodedUser decodedUser = jwtService.getUserDetails(token);
		System.out.println("Decoded User: " + decodedUser);

		Assertions.assertEquals(decodedUser.getId(), user.getId());
	}

	@Test
	void testJWTGeneration(){
		User user = User.builder()
				.id(13L)
				.fullName("TestUser1")
				.email("TestUser1@example.com")
				.build();

		String token = jwtService.generateJWT(user);

		Assertions.assertNotNull(token);
		Assertions.assertFalse(token.isEmpty());
	}

	@Test
	void testUserCreationPass(){
		UserSignUpRequest user = new UserSignUpRequest("TestUser1", "testUser1@example.com", "Password123");

		Assertions.assertNotNull(authService.signUpUser(user));
	}

	@Test
	void testUserCreation_Conflict(){
		UserSignUpRequest user = new UserSignUpRequest("TestUser1", "testUser1@example.com", "Password123");

		authService.signUpUser(user);

		UserSignUpRequest user2= new UserSignUpRequest("TestUser2", "testUser1@example.com", "Password123");

		Assertions.assertThrows(ConflictException.class, () -> authService.signUpUser(user2));
	}
}
