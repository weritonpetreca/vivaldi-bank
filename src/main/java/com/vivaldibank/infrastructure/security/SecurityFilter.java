package com.vivaldibank.infrastructure.security;

import com.vivaldibank.infrastructure.adapters.out.persistence.ContaEntity;
import com.vivaldibank.infrastructure.adapters.out.persistence.SpringDataContaRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public SecurityFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Tenta recuperar o token do cabeçalho
        String token = recoverToken(request);

        // 2. Se o token existir, valida ele
        if (token != null) {
            // A validação retorna o "Subject" (no nosso caso, o CPF)
            String cpf = tokenService.validarToken(token);

            if (!cpf.isEmpty()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(cpf);

                // Criamos um token "de dentro do Spring" (não é o JWT) para dizer que está logado
                var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

                // Salvamos no contexto: "O usuário X está logado nesta thread"
                SecurityContextHolder.getContext().setAuthentication((authentication));
            }
        }

        // 6. Continua o fluxo (vai para o Controller ou para o próximo filtro)
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        // O cabeçalho vem como "Bearer ..."
        // Precisamos remover o prefixo "Bearer" para pegar só o token
        return authHeader.substring(7);
    }
}
