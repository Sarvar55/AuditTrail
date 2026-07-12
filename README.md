# AuditTrail API

## Project Overview

Stateless Spring Boot API for user/task operations and append-only admin activity monitoring.

## Architecture

Domain services never access the audit repository; `AuditWriter` is the only write gateway.

```text
Controller
    ↓
Business Service
    ↓
@Auditable
    ↓
AuditAspect
    ↓
AuditWriter
    ↓
PostgreSQL
```

## Tech Stack

Java 21, Spring Boot Web/Security/AOP/Data JPA, PostgreSQL, Flyway, JWT, Springdoc, Maven, JUnit and Testcontainers.

## Audit Flow

Failures are written immediately with `REQUIRES_NEW`; successes are written after commit. Audit infrastructure errors do not alter business results.

## Authentication Flow

```text
Authentication
    ↓
Spring Security Event
    ↓
AuthenticationAuditListener
    ↓
AuditWriter
```

## Package Structure

`domain` contains the audit, auth, task and user modules. Infrastructure is separated into top-level `config`, `security` and `exception` packages. Only reusable base response/entity types and constants remain under `common`. Entity-to-response conversions use MapStruct.

## Database Setup

Flyway creates users, tasks, audit events, indexes and append-only protection. Recreate development databases made with the previous V3 migration.

## Running with Docker

Run `docker compose up --build`. PostgreSQL uses `localhost:5433`; the API uses `localhost:8080`.

## Environment Variables

`DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION`, `ADMIN_EMAIL`, `ADMIN_PASSWORD`.

## API Endpoints

- `POST /api/v1/auth/register`, `POST /api/v1/auth/login`
- Task CRUD under `/api/v1/tasks`
- GET-only `/api/v1/audit-events/admin`

## Example Requests

```bash
curl -X POST localhost:8080/api/v1/auth/login -H 'Content-Type: application/json' -d '{"email":"admin@audittrail.local","password":"change-me-admin-password"}'
```

## Audit Search Examples

`GET /api/v1/audit-events/admin?actionType=TASK_DELETED&from=2026-07-01T00:00:00Z`

## Append-Only Strategy

The entity has no setters/update method, repository has only insert/read operations, and controller is GET-only. V5 rejects UPDATE/DELETE through a trigger. Apply its commented runtime grants after Flyway with a separate migration owner so migrations retain schema-change rights.

## Security Decisions

JWT is stateless. Auth is public; audit requires ADMIN at filter and method levels. Forwarded IP headers are trustworthy only behind a configured trusted proxy.

## Sensitive Data Rules

Audit records contain only actor ID, action, resource, IP address and timestamp. Request bodies, credentials and tokens are never recorded.

## Transaction Strategy

`AuditWriter.write` uses a small independent `REQUIRES_NEW` transaction.

## Testing

Run `./mvnw test`. PostgreSQL-specific repository and append-only tests should use Testcontainers; the lightweight H2 profile disables Flyway.

## Future Improvements

Cryptographic event chaining, retention/archive policies, an outbox, trusted-proxy allowlists and a read replica for large exports.
