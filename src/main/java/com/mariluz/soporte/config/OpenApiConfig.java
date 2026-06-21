package com.mariluz.soporte.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // Nombre interno del esquema de seguridad
    private static final String SCHEME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Servicio Soporte")
                .version("v1")
                .description("Microservicio de soporte - Tienda Mariluz")
                .contact(new Contact()
                    .name("Equipo Mariluz")
                    .email("contacto@mariluz.cl")))
            // hace que aparezca el candado y se envie el token en cada request
            .addSecurityItem(new SecurityRequirement().addList(SCHEME))
            .components(
                new Components().addSecuritySchemes(
                    SCHEME,
                    new SecurityScheme()
                        .name(SCHEME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            );
    }
}
