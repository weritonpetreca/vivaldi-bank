package com.vivaldibank.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vivaldi Bank API")
                        .version("1.0.0")
                        .description("API REST profissional para gerenciamento de contas bancárias." +
                                "Desenvolvida com Arquitetura Hexagonal, Spring Boot 3 e Java 21.")
                        .contact(new Contact()
                                .name("Weriton L. Petreca")
                                .email("eulcfr@gmail.com")
                                .url("http://weriton.dev"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
