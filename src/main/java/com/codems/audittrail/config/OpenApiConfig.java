package com.codems.audittrail.config;

import com.codems.audittrail.common.constants.ApplicationConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    OpenAPI openApi() {
        SecurityScheme bearer = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(new Info()
                        .title("AuditTrail API")
                        .description("Activity logging and security monitoring API")
                        .version(ApplicationConstants.API_VERSION))
                .components(new Components().addSecuritySchemes(BEARER_AUTH, bearer))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }

    @Bean
    OperationCustomizer versionHeader() {
        return (operation, handlerMethod) -> {
            HeaderParameter header = new HeaderParameter();
            header.setName(ApplicationConstants.API_VERSION_HEADER);
            header.setDescription("API version");
            header.setRequired(false);
            header.setSchema(new StringSchema()._default(ApplicationConstants.API_VERSION));
            operation.addParametersItem(header);
            return operation;
        };
    }
}
