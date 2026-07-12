package com.codems.audittrail.common.config;

import com.codems.audittrail.common.constants.ApplicationConstants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AuditTrail API",
                version = ApplicationConstants.API_VERSION,
                description = "Activity logging, task management, and security monitoring REST API.",
                contact = @Contact(name = "DevLab")
        ),
        security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
)
@SecurityScheme(
        name = OpenApiConfig.BEARER_AUTH,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "Provide the JWT access token obtained from /api/auth/login"
)
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    OpenApiCustomizer apiVersionHeaderCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(this::addApiVersionHeader)
        );
    }

    private void addApiVersionHeader(Operation operation) {
        if (operation.getParameters() == null) {
            operation.setParameters(new ArrayList<>());
        }

        boolean alreadyExists = operation.getParameters().stream()
                .anyMatch(parameter -> ApplicationConstants.API_VERSION_HEADER
                        .equalsIgnoreCase(parameter.getName()));
        if (alreadyExists) {
            return;
        }

        operation.addParametersItem(new Parameter()
                .in("header")
                .name(ApplicationConstants.API_VERSION_HEADER)
                .required(false)
                .description("API version header")
                .schema(new StringSchema()
                        ._default(ApplicationConstants.API_VERSION)
                        .example(ApplicationConstants.API_VERSION)));
    }
}
