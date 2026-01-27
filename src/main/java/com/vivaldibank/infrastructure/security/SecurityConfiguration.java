package com.vivaldibank.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;

    public SecurityConfiguration(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    // 1. A Corrente de Filtros (O "Livro das Leis")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Desabilita proteção contra ataque de formulário (Inútil em API REST Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Diz ao Spring "Não guarde sessão/cookies"
                .authorizeHttpRequests(authorize -> authorize
                    // Rotas Públicas (Qualquer um acessa)
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()   // Para fazer login
                    .requestMatchers(HttpMethod.POST, "/contas").permitAll()       // Para criar conta
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html").permitAll()                                     // Documentação

                    // Rotas Administrativas (Só Admin) - Exemplo futuro
                    // .requestMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMIN")

                    // O resto exige estar logado
                    .anyRequest().authenticated()
                )
                // Adiciona o nosso filtro ANTES do filtro padrão do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // 2. O Gerente de Autenticação
    // O Spring precisa desse Bean exposto para que a gente possa chamar o "login" manualmente no Controller
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    // 3. O Criptógrafo de Senhas
    // Define que o algoritmo de hash padrão do sistema é o BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
