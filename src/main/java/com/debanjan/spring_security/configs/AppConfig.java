package com.debanjan.spring_security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AppConfig {

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
    // passing some dummy users
        UserDetails user1 = User
                    .withUsername("Tom")
                    .password(passwordEncoder.encode("TomPassword"))
                    .roles("USER")
                    .build();

        UserDetails user2 = User
                .withUsername("Jerry")
                .password(passwordEncoder.encode("JerryPassword2$"))
                .roles("USER")
                .build();

        UserDetails user3 = User
                .withUsername("Debanjan")
                .password(passwordEncoder.encode("Password"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1, user2, user3);
    }
}
