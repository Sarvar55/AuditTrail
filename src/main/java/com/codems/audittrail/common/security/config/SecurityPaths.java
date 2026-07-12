package com.codems.audittrail.common.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.codems.audittrail.common.constants.ApplicationConstants.API_PATH_PREFIX;

@Configuration
public class SecurityPaths {

    @Bean("publicPaths")
    List<String> publicPaths() {
        return List.of(
                API_PATH_PREFIX + "/auth/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**"
        );
    }

    @Bean("securedPaths")
    List<String> securedPaths() {
        return List.of(
                API_PATH_PREFIX + "/tasks/**",
                API_PATH_PREFIX + "/users/me/**"
        );
    }

    @Bean("adminPaths")
    List<String> adminPaths() {
        return List.of(
                API_PATH_PREFIX + "/tasks/admin/**",
                API_PATH_PREFIX + "/users/admin/**",
                API_PATH_PREFIX + "/audit-events/admin/**"
        );
    }
}
