# 🏦 Vivaldi Bank API

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)

Uma API REST robusta e escalável para operações bancárias, desenvolvida com foco em **Arquitetura Hexagonal**, **Clean Code** e **Segurança**.

## 🚀 Sobre o Projeto

O Vivaldi Bank é um sistema de gerenciamento de contas correntes que suporta operações de alta concorrência garantindo a integridade transacional.

### 🏗️ Arquitetura
O projeto segue estritamente a **Arquitetura Hexagonal (Ports and Adapters)** para isolar o domínio da infraestrutura:
- **Domain:** Regras de negócio puras (Entidades, Validadores).
- **Application:** Casos de uso (UseCases) e Portas de Entrada/Saída.
- **Infrastructure:** Adaptadores Web (Controllers), Persistência (Spring Data JPA) e Configurações.

## 🛠️ Tech Stack

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3
- **Banco de Dados:** PostgreSQL 16
- **Migrations:** Flyway
- **Testes:** JUnit 5, Mockito & **Testcontainers** (Integração com banco real)
- **Documentação:** SpringDoc OpenAPI (Swagger UI)
- **Containerização:** Docker & Docker Compose

## ⚙️ Como Executar

### Pré-requisitos
- Docker e Docker Compose instalados.
- JDK 21 instalado.

### Passo a Passo
1. Clone o repositório:
```bash
git clone [https://github.com/SEU-USUARIO/vivaldi-bank.git](https://github.com/SEU-USUARIO/vivaldi-bank.git)
```

2. Suba o ambiente (Banco de Dados):
```bash
docker-compose up -d
```

3. Execute a aplicação:
```bash
./gradlew bootRun
```

4. Acesse a Documentação (Swagger):
   👉 http://localhost:8080/swagger-ui/index.html

## 🧪 Testes

O projeto conta com uma suíte de testes robusta:
- **Unitários:** Isolados com Mockito.
- **Integração:** End-to-end usando **Testcontainers** para subir um PostgreSQL temporário via Docker, garantindo que o teste rode em ambiente idêntico à produção.

Para rodar os testes:
```bash
./gradlew test
```

## 🛡️ Funcionalidades

- [x] **Gestão de Contas:** Criação e Consulta (com sanitização de CPF).
- [x] **Transações:** Depósitos, Saques e Transferências entre contas.
- [x] **Extrato:** Histórico completo com identificação da contraparte.
- [x] **Segurança:** Bloqueio Pessimista (`PESSIMISTIC_WRITE`) no banco para evitar condições de corrida em transações simultâneas.
- [x] **Audit:** Rastreabilidade de logs de movimentação.

## 🧙‍♂️ Autor

Desenvolvido por **Weriton L. Petreca** como parte de um estudo avançado em Engenharia de Software Backend.