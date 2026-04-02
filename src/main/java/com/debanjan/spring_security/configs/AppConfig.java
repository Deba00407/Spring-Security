package com.debanjan.spring_security.configs;

import com.debanjan.spring_security.entities.UserEntity;
import com.debanjan.spring_security.utils.SpringAuditImpll;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AppConfig {

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuditorAware<UserEntity> auditorProvider(){
        return new SpringAuditImpll();
    }
}
