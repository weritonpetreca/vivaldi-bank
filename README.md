# 🐺 Vivaldi Bank API

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![AWS SQS](https://img.shields.io/badge/AWS_SQS-FF9900?style=for-the-badge&logo=amazon-aws&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Security](https://img.shields.io/badge/Spring_Security-JWT-red?style=for-the-badge&logo=spring-security&logoColor=white)

> "Os bancos Vivaldi garantem o seu ouro melhor do que as muralhas de Kaer Morhen."

Uma API bancária robusta e escalável, forjada com **Arquitetura Hexagonal (Ports and Adapters)**, práticas de **Clean Code** e **Event-Driven Architecture**. Este projeto simula as operações do famoso banco de Novigrad, garantindo que cada Coroa seja contabilizada com precisão élfica.

---

## 📜 O Contrato (Funcionalidades)

O sistema oferece um conjunto completo de serviços financeiros, protegidos contra monstros e falhas de sistema:

### 💰 Gestão de Coroas (Transações)
*   **Depósitos:** Aporte de fundos seguro.
*   **Saques:** Retirada de valores (com validação de saldo).
*   **Transferências:** Movimentação entre contas Vivaldi (atômicas e seguras).
*   **Busca de Contas:** Localização por **ID (UUID)** ou **Número da Conta**.

### 📨 Mensageria e Eventos (The Megascope)
O sistema utiliza **AWS SQS** (simulado via LocalStack) para comunicação assíncrona e desacoplada:
*   **Evento de Login:** Notifica sistemas de auditoria/risco.
*   **Evento de Conta Criada:** Dispara processos de onboarding.
*   **Evento de Movimentação:** Registra logs de auditoria para cada transação.

### 🛡️ Segurança (Sinal Quen)
*   **Autenticação Stateless:** JWT (JSON Web Token).
*   **Criptografia:** Senhas hashadas com BCrypt.
*   **Validação:** CPF e regras de negócio estritas.

---

## 🏰 Arquitetura Hexagonal

O projeto segue estritamente a separação de responsabilidades, garantindo que o núcleo (Domínio) permaneça puro como prata.

*   **Domain (O Núcleo):** Entidades (`Conta`, `Movimentacao`) e Exceptions personalizadas (`ContaNaoEncontradaException`, `SaldoInsuficienteException`).
*   **Application (Os Bruxos):** Casos de Uso (`RealizarTransferencia`, `BuscarPorNumero`, etc.).
*   **Infrastructure (O Mundo):**
    *   *Web:* Controllers REST e Swagger.
    *   *Persistence:* PostgreSQL e Spring Data JPA.
    *   *Messaging:* SQS Adapter e Consumers.

---

## 🚀 Tecnologias e Ferramentas

*   **Linguagem:** Java 21 (LTS)
*   **Framework:** Spring Boot 3
*   **Mensageria:** AWS SQS + LocalStack
*   **Banco de Dados:** PostgreSQL 16 + Flyway (Migrations)
*   **Segurança:** Spring Security 6 + JWT
*   **Containerização:** Docker & Docker Compose
*   **Documentação:** OpenAPI 3 (Swagger UI)

---

## 🐳 Como Executar (Docker)

Para levantar o ambiente completo (API + Banco + LocalStack), certifique-se de ter o **Docker** instalado.

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/vivaldi-bank.git
    cd vivaldi-bank
    ```

2.  **Inicie os containers:**
    ```bash
    docker compose up -d
    ```
    *Isso subirá o PostgreSQL, o LocalStack (SQS) e a API.*

3.  **Acesse a documentação:**
    A API estará rodando em: `http://localhost:8080/swagger-ui/index.html`

---

## 🛠️ Endpoints Principais (O Quadro de Avisos)

| Método | Rota | Descrição | Auth |
| :--- | :--- | :--- | :---: |
| `POST` | `/auth/login` | Realiza login e retorna Token JWT | 🔓 |
| `POST` | `/contas` | Cria nova conta (Auto-login incluso) | 🔓 |
| `GET` | `/contas/{id}` | Busca conta por ID | 🔒 |
| `GET` | `/contas/numero/{numero}` | Busca conta por Número | 🔒 |
| `POST` | `/contas/{id}/deposito` | Realiza depósito | 🔒 |
| `POST` | `/contas/{id}/saque` | Realiza saque | 🔒 |
| `POST` | `/contas/transferencia` | Realiza transferência entre contas | 🔒 |

---

## 👨‍💻 Autor

Desenvolvido por **Weriton L. Petreca**

*   💼 [LinkedIn](https://www.linkedin.com/in/weriton-petreca)
*   📧 Contato: eulcfr@gmail.com

---

*"Wind's howling..."* 🍃
