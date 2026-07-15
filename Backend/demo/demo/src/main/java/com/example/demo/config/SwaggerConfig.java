package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("keycloak"))
                .components(
                        new Components()
                                .addSecuritySchemes("keycloak",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.OPENIDCONNECT)
                                                .openIdConnectUrl("http://localhost:8080/realms/webshop/.well-known/openid-configuration")
                                )
                );
    }
}

