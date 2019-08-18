package com.sung.local.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.AuthorizationScope;
import java.util.Arrays;
import java.util.List;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 * localhost:9090/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sung.local"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .useDefaultResponseMessages(true)
                .securitySchemes(getSecuritySchemes())
                .securityContexts(getSecurityContext());
    }

    private static List<? extends SecurityScheme> getSecuritySchemes() {

        return Arrays.asList(new ApiKey("Bearer", "Authorization", "header"));
    }

    private List<SecurityContext> getSecurityContext() {

        List<SecurityContext> securityContextList = Arrays.asList(SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build());
        return securityContextList;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Bearer",
                authorizationScopes));
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .docExpansion(DocExpansion.LIST)
                .build();
    }
}
