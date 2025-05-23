<p align="center">
  <img src="https://aaxjdkgyiasvrscexksn.supabase.co/storage/v1/object/public/devx-publics//devx-logo.png" width="350" alt="DevX Logo" />
</p>

# Zenvest Banking API 💸

A secure, robust banking and financial management backend built with **Spring Boot**. Designed for reliability and scalability, this project provides **user authentication**, **account management**, **transaction processing**, and **fund transfers** with complete security.

---

## ✨ Features

- **User Registration & Authentication** (JWT-based)
- **Account Management** (Create, View, Update)
- **Transaction Processing** (Deposits and Withdrawals)
- **Funds Transfer** between accounts
- **Transaction History**
- **Secure JWT Authentication** with token validation
- **Exception Handling** for robust error management

---

## 📆 Technologies

- Java 21
- Spring Boot 3.4.5
- Spring Security
- JWT Authentication
- Spring Data JPA
- MySQL
- Hibernate
- Lombok
- Swagger/OpenAPI for documentation
- Dotenv for environment variables

---

## 🌐 API Endpoints

### 🔓 Authentication

| Method | Endpoint                         | Description                       |
|--------|----------------------------------|-----------------------------------|
| POST   | `/api/authenticate/auth/register`| Register a new user               |
| POST   | `/api/authenticate/auth/login`   | Login with email & password       |
| POST   | `/api/authenticate/auth/refresh` | Refresh authentication token      |

### 💰 Accounts

| Method | Endpoint                                | Description                      |
|--------|----------------------------------------|----------------------------------|
| GET    | `/api/authenticate/accounts`           | List all user accounts           |
| POST   | `/api/authenticate/accounts`           | Create a new account             |
| GET    | `/api/authenticate/accounts/{id}`      | Get account details              |
| PUT    | `/api/authenticate/accounts/{id}`      | Update account details           |

### 💸 Transactions

| Method | Endpoint                                                | Description                      |
|--------|--------------------------------------------------------|----------------------------------|
| GET    | `/api/authenticate/accounts/{accountId}/transactions`   | List all account transactions    |
| POST   | `/api/authenticate/accounts/{accountId}/transactions/deposit` | Deposit to account         |
| POST   | `/api/authenticate/accounts/{accountId}/transactions/withdraw` | Withdraw from account     |

### 📤 Transfers

| Method | Endpoint                                  | Description                            |
|--------|-------------------------------------------|----------------------------------------|
| POST   | `/api/authenticate/transfer`      | Transfer funds between accounts        |
| GET    | `/api/authenticate/transfer/history` | Get transfer history                 |

---

## 🎓 Project Structure

```
DevX/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── zenvest/
│   │   │           └── devx/
│   │   │               ├── DevXApplication.java
│   │   │               ├── constants/
│   │   │               │   ├── ApiEndpoint.java
│   │   │               │   └── TransactionType.java
│   │   │               ├── controllers/
│   │   │               │   ├── AccountController.java
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── TransactionController.java
│   │   │               │   └── TransferController.java
│   │   │               ├── dtos/
│   │   │               │   ├── inputs/
│   │   │               │   └── outputs/
│   │   │               ├── exceptions/
│   │   │               │   └── ZenvestExceptionHandler.java
│   │   │               ├── models/
│   │   │               │   ├── Account.java
│   │   │               │   ├── Transaction.java
│   │   │               │   ├── Transfer.java
│   │   │               │   └── User.java
│   │   │               ├── repositories/
│   │   │               │   ├── AccountRepository.java
│   │   │               │   ├── TransactionRepository.java
│   │   │               │   ├── TransferRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               ├── responses/
│   │   │               │   └── ZenvestResponse.java
│   │   │               ├── security/
│   │   │               │   └── SecurityConfiguration.java
│   │   │               ├── services/
│   │   │               │   ├── AccountService.java
│   │   │               │   ├── AuthService.java
│   │   │               │   ├── TransactionService.java
│   │   │               │   └── TransferService.java
│   │   │               └── utils/
│   │   │                   └── JwtService.java
│   │   └── resources/
│   │       └── application.yml
├── .env
├── .env.example
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

---

## ⚙️ Getting Started

### 1. Fork the repository on GitHub.
### 2. Clone your forked repository to your local machine:
   ```bash
      git clone https://github.com/<your-username>/<repo-name>.git
      cd <repo-name>
   ```
### 3. Setup Environment Variables
Create `.env` file in the root directory based on `.env.example`:
```yaml
# Application Info
SPRING_APPLICATION_NAME=DevX

# MySQL Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/zenvest?createDatabaseIfNotExist=true
SPRING_DATASOURCE_USERNAME=your_mysql_username
SPRING_DATASOURCE_PASSWORD=your_mysql_password

# Server Port
SERVER_PORT=8080

# JWT
JWT_SECRET=your_secret_key
JWT_ACCESS_TOKEN_EXPIRY_MS=3600000
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
```

---

The API will be available at `http://localhost:8080`

---

### 5. (Optional) Create a new branch.
```bash
   git checkout -b bugfix/bug-description
```
### 6. Fix a bug or bugs — there's no need to fix all of them to open a pull request.
### 7. Commit your changes
```bash
   git add .
   git commit -m "Fix: brief description of the bug fix"
```
### 6. Push your changes
```bash
   git push origin main
   ```

#### - (Optional) If you create a new branch, use
```bash
   git push origin bugfix/bug-description
  ```
### 7. Open a Pull Request from your forked repository to the main repository.

---

### Postman Collection Json: [Zenvest Collection](https://drive.google.com/file/d/1iftso7zT5my4OD5apvBi0nJjHCg3gbSy/view?usp=sharing)

---

## 🚀 Example Requests

### Register a new user
```http
POST /api/authenticate/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "Secure@123"
}
```

### Login
```http
POST /api/authenticate/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "Secure@123"
}
```

### Create a new account
```http
POST /api/authenticate/accounts
Content-Type: application/json
Authorization: Bearer YOUR_JWT_TOKEN

{
  "ownerName": "John Doe",
  "balance": 1000.00,
  "active": true
}
```

### Make a deposit
```http
POST /api/authenticate/accounts/1/transactions/deposit
Content-Type: application/json
Authorization: Bearer YOUR_JWT_TOKEN

{
  "amount": 500.00,
  "transactionType": "DEPOSIT",
  "description": "Salary deposit"
}
```

### Transfer funds
```http
POST /api/authenticate/accounts/transfer
Content-Type: application/json
Authorization: Bearer YOUR_JWT_TOKEN

{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 250.00,
  "description": "Rent payment"
}
```

---

## 📄 Environment Variables

| Key                       | Description                               |
|---------------------------|-------------------------------------------|
| `SPRING_APPLICATION_NAME` | Application name                          |
| `SPRING_DATASOURCE_URL`   | MySQL database connection URL             |
| `SPRING_DATASOURCE_USERNAME` | Database username                      |
| `SPRING_DATASOURCE_PASSWORD` | Database password                      |
| `SERVER_PORT`             | Server port (default: 8080)               |
| `JWT_SECRET`              | Secret key for JWT token generation       |
| `JWT_ACCESS_TOKEN_EXPIRY_MS` | JWT token expiration in milliseconds   |

---

## 📊 Database Schema

- `users`: User credentials and profile information
- `accounts`: Banking accounts with balance information
- `transactions`: Record of deposits and withdrawals
- `transfers`: Record of transfers between accounts

---

## 🛡️ Security Highlights

- JWT-based authentication
- Secure token validation
- Password encryption with BCrypt
- Cross-Origin Resource Sharing (CORS) configuration

---

## ⚠️ Note on Leaderboard Loading Time
The leaderboard is hosted on Render’s free plan, which spins down the server after 15 minutes of inactivity.

When you visit the leaderboard for the first time (or after some idle time), the server needs a moment to spin back up.

Please be patient and wait up to 1 minute for the leaderboard to load on your first visit.

After that, it will respond instantly until idle again.

---

### 🚀⭐ Smash that star button if you find this hackathon project awesome! [DevX- Repo](https://github.com/UdaraWickramarathne/devx-hackathon)

🔥 Huge thanks for checking it out — your support powers this project to the next level! Let's crush it! 💪

## 📃 License

[MIT](LICENSE.md) © 2025 — Udara Wickramarathne
