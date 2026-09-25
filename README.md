# spring-boot-dockerized-testcontainers

A Spring Boot API that proves; with a real, repeatable test, not a guess ; that its JPA layer has an N+1 query bug, fixes it, and ships the whole stack (app + Postgres) via Docker so it runs identically anywhere.

## Problem

"Works on my machine" is a common client complaint for APIs backed by JPA/Hibernate: a lazy @OneToMany relationship looks fine with 2-3 rows in a local test, then silently fires one extra query per parent row once real data volume hits production ; and nobody notices until performance complaints roll in. Most teams catch this by eyeballing SQL logs after the fact, if at all.

## Approach

1. Model a domain with a relationship that will realistically produce an N+1 problem ; Booking → List<BookingItem> (lazy @OneToMany, JPA default).
2. Define schema and seed data with Liquibase changelogs (chosen over Flyway here to demonstrate the other common enterprise migration tool, with its per-changeset rollback support).
3. Build entities, Spring Data repositories, a service layer with real logic, and REST controllers.
4. Write Mockito unit tests for the service layer, mocking the repository so business logic is verified independent of any database.
5. Write a "red" integration test first: spin up real Postgres via Testcontainers, fetch a list of bookings, access each one's lazily-loaded items, and assert on Hibernate's query-count statistic ; proving the N+1 exists instead of eyeballing logs.
6. Fix it with JOIN FETCH / @EntityGraph and re-run the same assertion: "green"; to prove the query count dropped.
7. Package with a multi-stage Dockerfile and a docker-compose.yml that wires the app to its own Postgres service, so the entire stack starts with one command.

## What I built

- Booking / BookingItem JPA entities with a lazy @OneToMany relationship
- Liquibase changelogs defining the schema and seeding multiple bookings, each with several items
- A service layer with real orchestration logic, covered by Mockito-based unit tests
- REST controllers exposing the booking domain
- A Testcontainers-based integration test asserting Hibernate's SessionFactory.getStatistics() query count; red before the fix, green after
- A multi-stage Dockerfile (JDK+Maven build stage, slim JRE runtime stage)
- A docker-compose.yml running the app alongside a Postgres service, configured entirely through environment variables

## Key decisions & tradeoffs

- *Testcontainers over H2*; H2's SQL dialect can silently mask Postgres-specific behavior (including how it plans and counts queries), so the integration test runs against the actual database engine the app ships with in production.
- *Liquibase over Flyway*; used here specifically to demonstrate the alternative to Flyway (used elsewhere in this portfolio),and because its per-changeset rollback model fits a schema that's expected to evolve.
- *Hibernate Statistics as the assertion, not log-reading*; turns "I fixed a performance bug" into a repeatable, automated check (assertEquals on query count) instead of a one-time manual observation that can regress silently later.
- *Mockito unit tests kept separate from Testcontainers integration tests* ; so it's always clear whether a given test is verifying business logic in isolation or actual database behavior.
- *No hardcoded credentials*; all DB connection details are environment-based (.env / .env.example), consistent with a lesson learned the hard way in an earlier repo.

## How to run it

````bash
# clone
git clone https://github.com/SanaShahSyeda/springboot-dockerized-testcontainer
cd springboot-dockerized-testcontainer
````

# configure environment
cp .env.example .env
# edit .env with your local DB credentials if not using docker-compose defaults

# run the full stack (app + Postgres)
docker-compose up


## Tests

- *Unit tests (Mockito)* ; service-layer business logic verified with a mocked repository, no database involved.
- *Integration tests (Testcontainers + Hibernate Statistics)* ; a real, throwaway Postgres container proves the N+1 bug and its fix via an explicit query-count assertion:

  | State | Query count for N bookings | Why |
    |---|---|---|
  | Before fix (red) | 1 + N | One query for bookings, one more per booking to lazily load its items |
  | After fix (green) | 1 | Single query via JOIN FETCH / @EntityGraph |

bash
mvn test


## Tech stack

- Language/framework: Java 21, Spring Boot, Spring Data JPA, Spring MVC
- Database: PostgreSQL (Testcontainers for tests, Docker Compose service for runtime)
- Infra/tooling: Liquibase, Docker (multi-stage build), Docker Compose, Mockito, JUnit 5, Hibernate Statistics