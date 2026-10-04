# JWT Authentication - Spring Boot 4 / Java 21

Multi-module Spring Boot application:

- `auth-backend`: Spring MVC REST API, Spring Security, custom JWT filter, JPA/Hibernate, PostgreSQL.
- `auth-frontend`: Spring Boot MVC + Thymeleaf login/registration UI.
- PostgreSQL runs in Docker Compose.

## Requirements

- Java 21
- Maven 3.9+
- Docker Desktop / Docker Engine + Compose

## Run locally from IntelliJ

Import the root folder as a Maven project.

Run:
- `com.example.auth.backend.AuthBackendApplication` on port 8081
- `com.example.auth.frontend.AuthFrontendApplication` on port 8080
- PostgreSQL locally on 5432, or run `docker compose up postgres -d`

Open http://localhost:8080/register

## Run everything with Docker Compose

First build the two JARs:

```bash
mvn clean package
```

Then:

```bash
docker compose up --build
```

Open http://localhost:8080/register

## API

POST `/api/auth/register`

```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "Password123"
}
```

POST `/api/auth/login`

```json
{
  "username": "john",
  "password": "Password123"
}
```

Response:

```json
{
  "accessToken": "...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "username": "john",
  "role": "USER"
}
```

Protected endpoint:

GET `/api/user/profile`

Header:

`Authorization: Bearer <accessToken>`

## Security

- Passwords are BCrypt-hashed.
- JWT is signed using an HMAC secret.
- JWT includes subject, role, issued-at, and expiration claims.
- Security is stateless.
- `/api/admin/**` requires ADMIN.
- `/api/user/**` requires USER or ADMIN.
- Invalid/expired/missing authentication results in authorization failure.
- CORS allows the frontend at `http://localhost:8080`.

## Important architecture note

This project intentionally uses Spring MVC + JPA/Hibernate because JPA/Hibernate is a mandatory requirement. JPA/JDBC database operations are blocking. Do not describe the complete stack as fully reactive/non-blocking.

For production, replace `ddl-auto=update` with Flyway/Liquibase migrations and supply the JWT secret through a real secrets manager/environment configuration.


What you can do in a browser:

• Open the app UI: http://localhost:8080/register

• Open API docs/health endpoints if exposed, but not the DB

To access the database:

• Use a DB client like pgAdmin, DBeaver, or psql

• Or from terminal:

◦ docker exec -it auth-postgres psql -U authuser -d authdb

◦ \dt

◦ SELECT * FROM users;

If the project is running, start it with:

• docker compose up --build Then browse:

• Frontend: http://localhost:8080

• Backend API: http://localhost:8081


## more info

Yes — in this project, PostgreSQL is configured in application.yml like this:

• DB host: localhost by default

• port: 5432

• database: authdb

• username: authuser

• password: authpassword

From the repo root, the quickest way is:

• Start PostgreSQL:   docker compose up postgres -d

• Connect to the DB: run from terminal

docker exec -it auth-postgres psql -U authuser -d authdb

Then inside psql:

• \dt → list tables

• SELECT * FROM users; → see registered users

• SELECT * FROM users WHERE username = 'john';

This project defines the table as users in auth-backend/src/main/java/com/example/auth/backend/entity/User.java.

