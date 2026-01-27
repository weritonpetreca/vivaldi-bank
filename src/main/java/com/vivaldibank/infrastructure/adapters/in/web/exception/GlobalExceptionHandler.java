package com.vivaldibank.infrastructure.adapters.in.web.exception;

import com.vivaldibank.domain.model.exception.ContaNaoEncontradaException;
import com.vivaldibank.domain.model.exception.CpfJaCadastradoException;
import com.vivaldibank.domain.model.exception.NumeroNaoEncontradoException;
import com.vivaldibank.domain.model.exception.SaldoInsuficienteException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleDomainError(IllegalArgumentException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage()
        );

        problemDetail.setTitle("Erro de Regra de Negócio");
        problemDetail.setType(URI.create("https://vivaldibank.com/erros/regra-de-negocio"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleWebValidationError(MethodArgumentNotValidException ex) {

        StringBuilder errors = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(": ")
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Dados de entrada inválidos"
        );

        problemDetail.setTitle("Erro de Validação de Dados");
        problemDetail.setProperty("detalhes", errors.toString());
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail dandleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String mensagemErro = ex.getMostSpecificCause().getMessage();

        if (mensagemErro != null && mensagemErro.contains("cpf")) {
            return criarProblema(
                HttpStatus.CONFLICT,
                "Conflito de Dados",
                "O CPF informado já possui cadastro no Vivaldi Bank."
            );
        }
        return criarProblema(
            HttpStatus.CONFLICT,
            "Conflito de Dados",
            "A operação viola uma regra de integridade de dados (ex: campo único duplicado)."
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericError(Exception ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno inesperado. Contate o suporte"
        );

        problemDetail.setTitle("Erro Interno");
        problemDetail.setProperty("timestamp", Instant.now());

        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<ProblemDetail> handleAccountNotFound(ContaNaoEncontradaException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        problemDetail.setTitle("Recurso não encontrado");
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(NumeroNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNumberNotFound(NumeroNaoEncontradoException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        problemDetail.setTitle("Número da conta não encontrada");
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ProblemDetail> handleInsuficientFound(SaldoInsuficienteException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage()
        );

        problemDetail.setTitle("Saldo insuficiente");
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problemDetail);
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ProblemDetail handleCpfJaCadastrado(CpfJaCadastradoException ex) {
        return criarProblema(
            HttpStatus.CONFLICT,
            "CPF já cadastrado",
            "O CPF informado já possui cadastro no Vivaldi Bank."
        );
    }

    private ProblemDetail criarProblema(HttpStatus status, String titulo, String detalhe) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detalhe);
        problemDetail.setTitle(titulo);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
