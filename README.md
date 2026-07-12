# AuditTrail API

AuditTrail is a Spring Boot REST API for JWT authentication, user-owned task management, and administrative activity monitoring.

## Tech Stack

- Java 17 and Spring Boot 4
- Spring Web, Security, AOP, Data JPA, and Validation
- PostgreSQL and Flyway
- JWT with JJWT and BCrypt
- MapStruct, Lombok, and Apache Commons CSV
- Springdoc OpenAPI
- Maven and Docker Compose

## Architecture

```text
Controller → Service → Repository → PostgreSQL
                     ↓
                 @Auditable
                     ↓
             AuditAspect → AuditWriter
```

Task and user services never access the audit repository directly. Audit writes go through `AuditWriter` in a separate transaction.

## API Usage

All application endpoints use `/api`. API version `1.0` can be sent with:

```http
X-API-Version: 1.0
Authorization: Bearer <token>
```

| Method | Path | Access |
|---|---|---|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| GET, POST | `/api/tasks` | Authenticated |
| GET, PUT, DELETE | `/api/tasks/{id}` | Authenticated |
| GET, PUT | `/api/users/me` | Authenticated |
| PUT | `/api/users/admin/{userId}/role` | ADMIN |
| GET | `/api/audit-events/admin` | ADMIN |
| GET | `/api/audit-events/admin/export` | ADMIN |

Audit search supports `actorId`, `actionType`, `resourceType`, `resourceId`, `from`, `to`, pagination, and sorting.

```http
GET /api/audit-events/admin?actionType=TASK_DELETED&resourceType=TASK&sort=createdAt,desc
```

Use `sort=field,direction`; do not send sort values as JSON arrays.

## Audit Data

Audit records contain actor ID, action, resource type/ID, IP address, and timestamp. Supported resources are `TASK`, `USER`, and `AUTHENTICATION`. Successful login is not audited; failed login is recorded automatically.

The audit API is read-only. Flyway V5 rejects database `UPDATE` and `DELETE` operations on `audit_events`.

## Environment Variables

| Variable | Default |
|---|---|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5433/audit-trail` |
| `DATABASE_USERNAME` | `user` |
| `DATABASE_PASSWORD` | `12345` |
| `JWT_SECRET` | Development Base64 secret |
| `JWT_EXPIRATION` | `3600000` |
| `PORT` | `8080` |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:3000` |
| `AUDIT_TRAIL_SEED_ENABLED` | `true` |
| `ADMIN_EMAIL` | `admin@gmail.com` |
| `ADMIN_PASSWORD` | `admin@@1234!!` |
| `USER_EMAIL` | `demo@example.com` |
| `DEMO_PASSWORD` | `password` |

Replace all default credentials and secrets, disable seed users, restrict CORS, and configure trusted proxies before production deployment.

## Run

```bash
cp .env.example .env.db.dev
docker compose up --build
```

API: `http://localhost:8080` — PostgreSQL: `localhost:5433`

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Tests

```bash
./mvnw test
./mvnw clean verify
```

## Important Rules

- Never log or store plaintext passwords, JWTs, authorization headers, or request bodies.
- Never expose write or delete endpoints for audit records.
- Never grant audit table UPDATE, DELETE, or TRUNCATE privileges to the runtime user.
- Trust forwarded IP headers only behind a trusted reverse proxy.
- Add new Flyway migrations instead of editing migrations already applied in shared environments.
- CSV export currently loads matching records into memory; use streaming for large datasets.
