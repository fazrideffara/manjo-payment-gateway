package com.manjo.paymentgateway.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String jwtSchemeName = "JWT";
        final String signatureSchemeName = "X-Signature";
        
        return new OpenAPI()
                .info(new Info()
                        .title("Manjo Payment Gateway API")
                        .description("API Dokumentasi untuk sistem Payment Gateway PT Manjo Teknologi Indonesia.")
                        .version("v1.0.0"))
                .components(new Components()
                        .addSecuritySchemes(jwtSchemeName,
                                new SecurityScheme()
                                        .name(jwtSchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        .addSecuritySchemes(signatureSchemeName,
                                new SecurityScheme()
                                        .name("X-Signature")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)));
    }
}
