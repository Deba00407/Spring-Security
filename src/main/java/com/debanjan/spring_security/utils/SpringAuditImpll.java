package com.debanjan.spring_security.utils;

import com.debanjan.spring_security.entities.UserEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringAuditImpll implements AuditorAware<UserEntity> {
    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(context -> context.getAuthentication())
                .map(auth -> (UserEntity) auth.getPrincipal())
                .map(UserEntity.class::cast);

    }
}
