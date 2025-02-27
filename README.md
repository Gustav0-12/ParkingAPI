# ParkingAPI

ParkingAPI é uma API para gerenciamento de estacionamentos, permitindo registrar, atualizar e monitorar veículos em tempo real.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **Hibernate**
- **MySQL**
- **Docker & Docker Compose**
- **Postman para testar requisições**

## Instalação e Configuração

### 1. Clonar o Repositório

```sh
git clone https://github.com/Gustav0-12/ParkingAPI.git
cd ParkingAPI
```

### 2. Configurar o Banco de Dados

A API utiliza MySQL como banco de dados. Certifique-se de que o MySQL esteja rodando e crie um banco de dados:

```sql
CREATE DATABASE parking_db;
```

Altere o arquivo `application.properties` ou defina as variáveis de ambiente:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/parking_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Construir e Executar a API

#### Executando com Maven:

```sh
mvn clean install
mvn spring-boot:run
```

## Endpoints

### Autenticação

#### Registrar
```http
POST /auth/register
```
**Corpo da Requisição:**
```json
{
  "name": "your_name",
  "email": "email@example.com"
  "password": "your_password",
  "cpf": "your_cpf",
  "role": "COMMON/ADMIN"  
}
```

#### Login

```http
POST /auth/login
```

**Corpo da Requisição:**

```json
{
  "email": "admin",
  "password": "admin"
}
```

**Resposta:**

```json
{
  "token": "jwt-token-gerado"
}
```

### Vagas

#### Registrar Vagas

```http
POST /parkingspot/create
```

**Corpo da Requisição:**

```json
{
  "code":"A-02",
  "status":"AVAILABLE"
}
```

#### Listar Vagas

```http
GET /parkingspot/{code}
```

### Estacionamento

#### Check-In

```http
POST /parking/check-in
```

**Corpo da Requisição:**
```json
{
    "marca":"Fiat",
    "cor":"Prata",
    "modelo":"Uno",
    "placa":"123456789",
    "userCPF":"user_cpf"
}
```

**Resposta:**

```json
{
    "marca": "Volks",
    "cor": "Prata",
    "modelo": "Polo",
    "placa": "123456789",
    "userCpf": "user_cpf",
    "recibo": "334307",
    "valor": 0,
    "dataEntrada": "27-02-2025 15:31:39",
    "dataSaida": null,
    "vagaCodigo": "A-01"
}
```

#### Check-Out
```http
POST /parking/check-out/{recibo}
```

**Resposta:**

```json
{
    "marca": "Volks",
    "cor": "Prata",
    "modelo": "Polo",
    "placa": "123456789",
    "userCpf": "user_cpf",
    "recibo": "334307",
    "valor": 5.0,
    "dataEntrada": "27-02-2025 15:31:40",
    "dataSaida": "27-02-2025 15:31:54",
    "vagaCodigo": "A-01"
}
```

#### Encontrar pelo recibo (Se a data de saida for nula)
```http
GET /parking/{recibo}
```

#### Encontrar pelo cpf do usuário
```http
GET /parking/user/{cpf}
```




