package com.vivaldibank.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Vivaldi Bank API",
        version = "1.0.0",
        description = "API Enterprise para gestão de contas bancárias, transações e segurança de alta performance.",
        // AQUI ESTÁ A MÁGICA DO CONTATO E SITE 👇
        contact = @Contact(
            name = "Weriton L. Petreca | Mestre Bruxo",
            email = "eulcfr@gmail.com",
            url = "https://www.linkedin.com/in/weriton-petreca" // Coloque seu LinkedIn ou Portfólio aqui
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        ),
        termsOfService = "https://vivaldibank.com/terms"
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Ambiente de Desenvolvimento (Local)"),
        @Server(url = "https://api.vivaldibank.com", description = "Ambiente de Produção")
    },
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Insira o token JWT obtido no login ou criação de conta."
)
public class OpenApiConfig {}
