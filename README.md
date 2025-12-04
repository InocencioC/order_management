

# 📚 Order Management System

## Visão Geral do Projeto

O **SIBS Order Management System** é uma API RESTful desenvolvida em Spring Boot para gerir o ciclo de vida de **utilizadores**, **itens**, **pedidos** (orders) e **stock** (stock movements). O sistema foca-se na **automação do *fulfillment*** de pedidos e na **rastreabilidade** de todas as operações.

-----

## 💻 Tecnologias

| Área | Tecnologia | Versão |
| :--- | :--- | :--- |
| **Backend** | Spring Boot | 2.7.x |
| **Linguagem** | Java | 8 |
| **Base de Dados** | PostgreSQL | 15+ (via Docker) |
| **Documentação** | Swagger / OpenAPI 2 | N/A |

-----

## ⚙️ Configuração e Execução

### Pré-requisitos

1.  **Java Development Kit (JDK 8)**
2.  **Maven**
3.  **Docker** e **Docker Compose**

### Passo 1: Inicialização do PostgreSQL (Docker Local)

Para o **desenvolvimento local**, use o `docker-compose.yml` para levantar o contentor PostgreSQL, porém já está configurado um banco na nuvem, e o recomendado a usar nesta app.

```bash
docker compose up -d
```

| Parâmetro | Valor |
| :--- | :--- |
| **Porta** | `5432:5432` |
| **Nome do BD** | `order_db` |
| **Usuário** | `sibs_user` |
| **Senha** | `password` |

### Passo 2: Configuração da Aplicação


1.  Abra `src/main/resources/application.properties`.
    
2.  **Substitua** os *placeholders* das credenciais de e-mail:
    ```properties
    spring.mail.username=YOUR_SMTP_USERNAME_HERE
    spring.mail.password=YOUR_SMTP_KEY_HERE
    ```

### Passo 3: Construção e Execução

Construa o projeto com Maven e inicie a aplicação Spring Boot:

```bash
# Construir o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

-----

## 🌐 Documentação da API

A documentação detalhada da API está disponível via **Swagger UI**.

  * **URL do Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

## 🔗 Endpoints da API

A URL base para todos os endpoints é `http://localhost:8080/api/v1`.

### 1\. 👥 Gestão de Utilizadores (`/users`)

| Método | Endpoint | Descrição | Corpo da Requisição (JSON) |
| :--- | :--- | :--- | :--- |
| **POST** | `/users` | Cria um novo utilizador. | `{"name": "inoCR", "email": "inoCR@gmail.com"}` |
| **GET** | `/users/{id}` | Busca utilizador por ID. (Ex: `08ba0d0c-6982-47bb-9b5e-fa0b484b27e5`) | N/A |
| **GET** | `/users` | Lista todos os utilizadores. | N/A |
| **PUT** | `/users/{id}` | Atualiza nome e/ou email. (Ex: `{"name": "inocencio1", "email": "ino75"}`) | `{"name": "...", "email": "..."}` |
| **DELETE**| `/users/{id}` | Elimina utilizador por ID. | N/A |

### 2\. 📦 Gestão de Itens (`/items`)

| Método | Endpoint | Descrição | Corpo da Requisição (JSON) |
| :--- | :--- | :--- | :--- |
| **POST** | `/items` | Cria um novo item. | `{"name": "MB Way Products"}` |
| **GET** | `/items` | Lista todos os itens. | N/A |
| **GET** | `/items/{id}` | Busca item por ID. | N/A |
| **PUT** | `/items/{id}` | Atualiza o item. (Ex: `{"name": "MB Way Products 1"}`) | `{"name": "..."}` |
| **DELETE**| `/items/{id}` | Elimina item por ID. | N/A |

### 3\. 📈 Movimentos de Stock (`/stock-movements`)

| Método | Endpoint | Descrição | Corpo da Requisição (JSON) |
| :--- | :--- | :--- | :--- |
| **POST** | `/stock-movements` | Cria um movimento de stock (Entrada ou Saída). **Dispara a reconciliação de pedidos pendentes se Qtd \> 0.** | `{"itemId": "...", "quantity": 300}` |

### 4\. 📝 Gestão de Pedidos (`/orders`)

| Método | Endpoint | Descrição | Corpo da Requisição (JSON) |
| :--- | :--- | :--- | :--- |
| **POST** | `/orders` | Cria e tenta satisfazer o pedido. | `{"userId": "...", "itemId": "...", "quantity": 100}` |
| **GET** | `/orders` | Lista todos os pedidos. | N/A |
| **GET** | `/orders/{id}` | Busca pedido por ID, retornando o status. | N/A |
| **GET** | `/orders/{id}/completion` | Retorna a percentagem de conclusão do pedido. | N/A |
