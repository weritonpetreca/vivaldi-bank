# 🐺 Vivaldi Bank API

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Security](https://img.shields.io/badge/Spring_Security-JWT-red?style=for-the-badge&logo=spring-security&logoColor=white)

> "Steel for humans, Silver for monsters, and Clean Code for software."

Uma API bancária robusta e escalável, desenvolvida com foco em **Arquitetura Hexagonal (Ports and Adapters)**, práticas de **Clean Code** e **Segurança Enterprise**. Este projeto simula operações bancárias essenciais com um núcleo de domínio isolado e infraestrutura plugável.

---

## 🚀 Tecnologias e Ferramentas

O projeto foi forjado utilizando as melhores ferramentas do mercado:

* **Linguagem:** Java 21 (LTS)
* **Framework:** Spring Boot 3
* **Arquitetura:** Hexagonal (Ports & Adapters)
* **Segurança:** Spring Security 6 + JWT (Auth0) + BCrypt
* **Banco de Dados:** PostgreSQL 16
* **Migração de Dados:** Flyway
* **Containerização:** Docker & Docker Compose
* **Documentação:** OpenAPI 3 (Swagger UI)
* **Build Tool:** Gradle (Kotlin DSL)

---

## 🏰 Arquitetura

O projeto segue estritamente a **Arquitetura Hexagonal**, garantindo que a regra de negócio (Domínio) não dependa de frameworks ou bibliotecas externas.

* **Domain:** O núcleo puro. Contém as Entidades (`Conta`, `Movimentacao`) e as Portas de Entrada/Saída.
* **Application:** Casos de Uso (`CriarContaUseCase`, `BuscarContaPorIdUseCase`).
* **Infrastructure:** Adaptadores para o mundo externo (Controllers REST, Persistência JPA, Segurança, Swagger).

---

## 🛡️ Segurança (Auth & JWT)

A fortaleza do Vivaldi Bank é protegida por um sistema de autenticação Stateless via **JSON Web Token (JWT)**.

1.  **Criptografia:** Senhas são salvas no banco utilizando hash **BCrypt**.
2.  **Fluxo de Login:** O usuário troca credenciais (CPF/Senha) por um Token JWT assinado.
3.  **Proteção de Rotas:**
    * 🔓 **Públicas:** Criação de conta, Login e Documentação (Swagger).
    * 🔒 **Privadas:** Todas as operações bancárias (exigem Header `Authorization: Bearer <token>`).
4.  **Auto-Login:** Ao criar uma conta, o sistema já retorna o Token de acesso para onboarding imediato.

---

## 🐳 Como Executar (Docker)

Para levantar o ambiente completo (Aplicação + Banco de Dados), certifique-se de ter o **Docker** e o **Docker Compose** instalados.

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/vivaldi-bank.git](https://github.com/seu-usuario/vivaldi-bank.git)
    cd vivaldi-bank
    ```

2.  **Inicie os containers:**
    ```bash
    docker compose up -d
    ```
    *Isso subirá o PostgreSQL e a API automaticamente.*

3.  **Acesse a aplicação:**
    A API estará rodando em: `http://localhost:8080`

---

## 📖 Documentação da API (Swagger)

A documentação interativa está disponível via Swagger UI. Através dela, é possível testar todos os endpoints e realizar a autenticação.

📍 **Acesse:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Como Autenticar no Swagger:
1.  Use o endpoint `POST /auth/login` (ou crie uma conta nova) para obter o token.
2.  Copie o token (sem as aspas).
3.  Clique no botão **Authorize** (cadeado verde) no topo da página.
4.  Cole o token e clique em **Authorize**.

---

## 🛠️ Endpoints Principais

| Método | Rota | Descrição | Auth |
| :--- | :--- | :--- | :---: |
| `POST` | `/auth/login` | Realiza login e retorna Token JWT | 🔓 |
| `POST` | `/contas` | Cria nova conta (Auto-login incluso) | 🔓 |
| `GET` | `/contas/{id}` | Busca dados de uma conta por ID | 🔒 |
| `POST` | `/transacoes` | Realiza transferências/depósitos (Em breve) | 🔒 |

---

## 👨‍💻 Autor

Desenvolvido por **Weriton L. Petreca**

* 💼 [LinkedIn](https://www.linkedin.com/in/weriton-petreca)
* 📧 Contato: eulcfr@gmail.com

---

*"Wind's howling..."* 🍃
