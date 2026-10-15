# room-booking
App lets students book empty rooms safely while locking taken slots. Professors can also schedule lectures. Built with Spring Boot and PostgreSQL.

## Team
Name                 |       ID

Turgunbay Meiirbek   |   240107022

Daulet Aladi         |   240107102

Jubanissov Daniyar   |   240107038



# Container diagram:
![Container_diagram.jpg](src/main/resources/Container_diagram.jpg)




# Architecture style
The architecture we are using is Layered Modular Monolith. The Layered architecture was chosen due to the fixed order of access flow, and Modular Monolith architecture was added since some layers have to consist of several modules, hence the combination of the 2.

# Architecture Decision Record (ADR 1)

## Title: Concurrency Control & Slot Locking Strategy for Room Reservations

* **Status:** Accepted
* **Author:** Jubanissov Daniyar (240107038)
* **Date:** October 2026

---

### 1. Context
During peak booking windows (e.g., rush hours or opening of weekly schedules), multiple students frequently attempt to reserve the same empty room slot at the exact same second. Without proper concurrency control, race conditions can occur, leading to critical double-booking issues.

We need a strategy that guarantees strict data consistency (zero double-bookings) while maintaining low latency and system simplicity using our existing tech stack (Spring Boot & PostgreSQL).

---

### 2. Decision
We decided to implement **Pessimistic Locking (`SELECT ... FOR UPDATE`)** at the PostgreSQL database layer via Spring Data JPA `@Lock(LockModeType.PESSIMISTIC_WRITE)` within transactional boundaries (`@Transactional`).

When a reservation request arrives:
1. The application acquires a database write-lock on the specific room slot.
2. It verifies whether the slot is still free.
3. It creates the booking record and updates slot status.
4. The transaction commits and releases the lock.

---

### 3. Alternatives Considered

* **Alternative 1: Optimistic Locking (using `@Version` field)**
  * *Pros:* Higher read performance, no database-level lock overhead.
  * *Cons:* Under high contention for popular rooms, many concurrent booking requests would fail with `OptimisticLockException`, forcing retries and degrading user experience.

* **Alternative 2: Distributed Lock with Redis (Redlock)**
  * *Pros:* Very fast in-memory locking; offloads concurrency checks from PostgreSQL.
  * *Cons:* Introduces extra infrastructure complexity, operational overhead, and an additional single point of failure for a single-server/monolithic setup.

---

### 4. Consequences

#### Positive:
* **Strong Consistency:** Guarantees zero double-booking at the database transaction level.
* **Simplicity:** Leverages native Spring Data JPA and PostgreSQL locking mechanisms without external middleware.
* **Predictability:** Failed attempts are handled gracefully with immediate feedback to the student that the slot was just taken.

#### Negative:
* **Database Contention:** Heavy concurrent requests for the exact same room slot will queue at the database connection pool level. However, given our campus domain scale, this latency overhead is negligible compared to the data integrity guarantee.

## Repository structure

```
room-booking/
├── src/            # Spring Boot code
├── pom.xml
└── README.md
```

# How to run
Instructions will be added after the first working version.
Requirements: Java 21, Maven.


