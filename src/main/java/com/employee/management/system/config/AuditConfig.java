package com.employee.management.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();


            if (auth == null || !auth.isAuthenticated()
                    || auth instanceof AnonymousAuthenticationToken) {
                return Optional.of("system");
            }

            Object principal = auth.getPrincipal();

            if (principal instanceof String s) {
                return Optional.of(s);
            }

            if (principal instanceof UserDetails ud) {
                return Optional.ofNullable(ud.getUsername());
            }

            return Optional.of("system");
        };
    }
}

