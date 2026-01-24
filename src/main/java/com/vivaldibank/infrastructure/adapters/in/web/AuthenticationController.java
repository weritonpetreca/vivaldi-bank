package com.vivaldibank.infrastructure.adapters.in.web;

import com.vivaldibank.infrastructure.adapters.in.web.dto.LoginRequest;
import com.vivaldibank.infrastructure.adapters.in.web.dto.LoginResponse;
import com.vivaldibank.infrastructure.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para login e gestão de tokens")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica o usuário via CPF e Senha e retorna um token JWT")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        // 1. Encapsulamos as credenciais (CPF e Senha plana) em um objeto do Spring
        // Lembre-se UsernamePasswordAuthenticationToken não é o JWT, é só um "envelope" de credenciais
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            request.cpf(),
            request.senha()
        );

        // 2. O authenticationManager pega esse envelope, bate no AuthenticacaoService,
        // recupera o hash do banco, compara com a senha plana usando BCrypt.
        // Se a senha estiver errada, ele lança uma Exception e retorna 403 Forbidden automaticamente
        Authentication auth = authenticationManager.authenticate(authenticationToken);

        // 3. Se passou da linha acima a senha está correta
        // Extrai o principal (usuário logado) de forma limpa
        // getName() retorna o "username" (que no nosso caso é o CPF) configurado no UserDetails
        String cpfAutenticado = auth.getName();

        String tokenJWT = tokenService.gerarToken(cpfAutenticado);

        return ResponseEntity.ok(new LoginResponse(tokenJWT));
    }
}
