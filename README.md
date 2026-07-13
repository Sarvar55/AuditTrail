# AuditTrail

AuditTrail is a Spring Boot REST API for JWT authentication, user-owned task management, profile administration, and immutable activity monitoring.

The application source lives in the project root directory.

## Tech Stack

- Java 17
- Spring Boot 4.1
- Spring Security
- Spring Data JPA
- Spring AOP
- PostgreSQL 18
- Flyway
- JWT
- MapStruct and Lombok
- Apache Commons CSV
- springdoc-openapi

## Requirements

- Java 17
- Maven 3.9+ or the bundled `mvnw`
- PostgreSQL 18
- Docker and Docker Compose for containerized runs

## Local Run

Start a PostgreSQL instance and make sure its connection settings match the configured environment variables. The default local database is available at `localhost:5433` with database name `audit-trail`.

```bash
./mvnw spring-boot:run
```

The API runs on `http://localhost:8080`.

By default, the application starts with the `dev` profile:

```properties
spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}
```

You can override it with:

```bash
SPRING_PROFILES_ACTIVE=prod ./mvnw spring-boot:run
```

## Docker Run

```bash
docker compose up --build
```

Services:

- `db` on `localhost:5433`
- `app` on `localhost:8080`

To stop the services:

```bash
docker compose down
```

## Environment Variables

### Application

| Variable | Required | Default | Description |
|---|---:|---|---|
| `SPRING_PROFILES_ACTIVE` | No | `dev` | Active Spring profile |
| `DATABASE_URL` | No | `jdbc:postgresql://localhost:5433/audit-trail` | JDBC URL for PostgreSQL |
| `DATABASE_USERNAME` | No | `user` | Database username |
| `DATABASE_PASSWORD` | No | `12345` | Database password |
| `JWT_SECRET` | No | Development Base64 secret | Base64-encoded secret used to sign JWTs |
| `JWT_EXPIRATION` | No | `3600000` | Access token lifetime in milliseconds |
| `API_VERSION_HEADER` | No | `X-API-Version` | Request header used for API versioning |
| `CORS_ALLOWED_ORIGINS` | No | `http://localhost:3000` | Allowed frontend origins |
| `CORS_ALLOW_CREDENTIALS` | No | `true` | Whether CORS allows credentials |
| `CORS_ALLOWED_METHODS` | No | `*` | Allowed HTTP methods |
| `CORS_ALLOWED_HEADERS` | No | `*` | Allowed request headers |
| `CORS_MAX_AGE` | No | `3600` | CORS cache age in seconds |
| `JPA_SHOW_SQL` | No | `false` | Whether Hibernate SQL is logged |
| `JPA_FORMAT_SQL` | No | `false` | Whether logged SQL is formatted |
| `AUDIT_TRAIL_SEED_ENABLED` | No | `true` | Whether development users are seeded |
| `ADMIN_NAME` | No | `admin` | Seed admin name |
| `ADMIN_EMAIL` | No | `admin@gmail.com` | Seed admin email |
| `ADMIN_PASSWORD` | No | `admin@@1234!!` | Seed admin password |
| `USER_EMAIL` | No | `demo@example.com` | Seed standard-user email |
| `DEMO_PASSWORD` | No | `password` | Seed standard-user password |
| `PORT` | No | `8080` | HTTP port |

### Database Container

| Variable | Required | Default | Description |
|---|---:|---|---|
| `POSTGRES_DB` | Yes | - | Database name |
| `POSTGRES_USER` | Yes | - | Database user |
| `POSTGRES_PASSWORD` | Yes | - | Database password |

Replace all development credentials and secrets, disable seed users, and restrict CORS before a production deployment.

## Docker Environment File

`.env.db.dev` controls the PostgreSQL container. The Docker Compose application service also reads this file and supplies its own database connection settings.

## API Notes

- Base API path: `/api`
- Swagger UI: `/swagger-ui.html`
- OpenAPI docs: `/v3/api-docs`
- Auth header: `Authorization: Bearer <token>`

## API Versioning

The API uses header-based versioning. The default and currently supported version is `1.0`, so clients can call endpoints without specifying a version or explicitly request it with:

```http
X-API-Version: 1.0
```

This keeps endpoint paths stable, for example:

```http
POST /api/auth/login
```

instead of embedding the version in the URL.

## Profiles

The default `dev` profile is intended for local development. A different profile can be selected with `SPRING_PROFILES_ACTIVE`; environment-specific secrets and production-safe settings should be supplied externally.

## Useful Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | Public | Register a user |
| `POST` | `/api/auth/login` | Public | Authenticate and receive a JWT |
| `GET` | `/api/tasks` | Authenticated | List the current user's tasks |
| `POST` | `/api/tasks` | Authenticated | Create a task |
| `GET` | `/api/tasks/{id}` | Authenticated | Get an owned task |
| `PUT` | `/api/tasks/{id}` | Authenticated | Update an owned task |
| `DELETE` | `/api/tasks/{id}` | Authenticated | Delete an owned task |
| `GET` | `/api/users/me` | Authenticated | Get the current profile |
| `PUT` | `/api/users/me` | Authenticated | Update the current profile |
| `PUT` | `/api/users/admin/{userId}/role` | Admin | Change a user's role |
| `GET` | `/api/audit-events/admin` | Admin | Search audit events |
| `GET` | `/api/audit-events/admin/export` | Admin | Export audit events as CSV |

Audit search supports `actorId`, `actionType`, `resourceType`, `resourceId`, `from`, `to`, pagination, and sorting.

Example:

```http
GET /api/audit-events/admin?actionType=TASK_DELETED&resourceType=TASK&sort=createdAt,desc
```

Supported audit actions are `LOGIN_FAILED`, `PROFILE_UPDATED`, `TASK_CREATED`, `TASK_UPDATED`, `TASK_DELETED`, and `USER_ROLE_CHANGED`. Supported resource types are `TASK`, `USER`, and `AUTHENTICATION`.

## Audit Trail Behavior

Audit records contain the actor ID, action, resource type, resource ID, client IP address, and timestamp. Task changes, profile updates, role changes, and failed login attempts are recorded.

Audit writes pass through `AuditWriter` in a separate transaction. The audit API is read-only, and Flyway migration `V5` protects `audit_events` against database `UPDATE` and `DELETE` operations.

## Testing

Run the test suite from the project root:

```bash
./mvnw test
```

Run the full Maven verification lifecycle with:

```bash
./mvnw clean verify
```

The test suite uses Spring Boot Test, JUnit 5, Mockito, AssertJ, and Spring Security Test.

## Important Notes

- Never log or store plaintext passwords, JWTs, authorization headers, or request bodies.
- Never expose update or delete endpoints for audit records.
- Never grant the runtime database user `UPDATE`, `DELETE`, or `TRUNCATE` privileges on the audit table.
- Trust forwarded IP headers only when the application runs behind a trusted reverse proxy.
- Add new Flyway migrations instead of editing migrations already applied in shared environments.
- CSV export currently loads matching records into memory; use streaming for large datasets.
