package com.codems.audittrail.common.config;

import com.codems.audittrail.common.constants.ApplicationConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final String DEFAULT_VERSION = ApplicationConstants.API_VERSION;
    private static final String[] SUPPORTED_VERSIONS = {ApplicationConstants.API_VERSION};

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                .useRequestHeader(ApplicationConstants.API_VERSION_HEADER)
                .addSupportedVersions(SUPPORTED_VERSIONS)
                .setDefaultVersion(DEFAULT_VERSION);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(
                ApplicationConstants.API_PATH_PREFIX,
                controllerType -> controllerType.getPackageName().startsWith("com.codems.audittrail.domain")
        );
    }
}
