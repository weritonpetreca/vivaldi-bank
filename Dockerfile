# ==============================================================================
# Estágio 1: Build (A Forja)
# Usamos uma imagem com Gradle e JDK 21 para compilar o projeto
# ==============================================================================

FROM gradle:8.5-jdk21-alpine AS builder

WORKDIR /app

# Copia apenas os arquivos de configuração de dependência primeiro (para cachear as libs)
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle

# Copia o código fonte
COPY src ./src

# Compila o projeto pulando os testes (os testes já rodam no CI, aqui queremos velocidade)
# O flag --no-daemon economiza memória no build do Docker
RUN gradle bootJar --no-daemon


# ==============================================================================
# Estágio 2: Runtime (O Artefato Final)
# Usamos uma imagem leve (JRE) apenas para rodar a aplicação
# ==============================================================================

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Cria um grupo e usuário não-root por segurança (Best Practice Enterprise)
# "securityUser" é o usuário que vai rodar o processo Java
RUN addgroup -S vivaldi && adduser -S securityUser -G vivaldi

# Copia o JAR gerado no estágio anterior (builder)
COPY --from=builder /app/build/libs/*.jar app.jar

# Define o usuário que vai executar o comando (Segurança: nunca rodar como root se possível)
USER securityUser

# Expõe a porta da aplicação
EXPOSE 8080

# Comando de inicialização
# Adiciona flas para suportar containers (limites de memória) e performance
ENTRYPOINT ["java", "-jar", "app.jar"]
