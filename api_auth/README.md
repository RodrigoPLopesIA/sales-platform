# Sales Platform - Authentication API

This is the authentication microservice for the **Sales Platform**.  
It handles user registration, login, and JWT-based authentication.

## 🚀 Features

- User registration (`POST /api/v1/register`)
- User login (`POST /api/v1/login`)
- JWT token generation
- Input validation
- Swagger API documentation
- Modular service/controller structure
- TypeScript support

## 🛠️ Technologies Used

- Node.js
- Express
- TypeScript
- JWT (jsonwebtoken)
- bcrypt
- Celebrate and Joi (for request validation)
- TypeORM
- PostgreSQL
- Jest and Supertests - Unit Tests
- Swagger (OpenAPI)

## 📦 Installation

```bash
git clone https://github.com/RodrigoPLopesIA/sales-platform.git
cd api_auth
yarn install

```

## ⚙️ Environment Variables

```
PORT=8081
DB_HOST=localhost
DB_PORT=5432
DB_NAME=auth_db
DB_USER=admin
DB_PASSWORD=123456
NODE_ENV=development
JWT_SECRET=123456789
```

## ▶️ Running the Server

```
yarn dev
```

## 🧪 Running Tests

```
yarn test

```

## 📄 API Documentation

`http://localhost:8081/api-docs/`


