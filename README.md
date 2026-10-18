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

# Architecture Decision Record (ADR #1): Asynchronous Email Notifications

---

### 1. Context

Email delivery may be delayed or temporarily unavailable. Booking submissions and approval decisions should remain responsive even when notifications cannot be delivered immediately.

---

### 2. Decision

The system will process email notifications asynchronously after a booking request is submitted, approved, or rejected, allowing booking and approval operations to complete without waiting for email delivery.

---

### 3. Alternatives Considered

Send email notifications synchronously as part of the booking or approval operation. This would avoid the need to manage a queue of pending notifications, but slow email delivery would still delay the booking response.

---

### 4. Consequences

Booking and approval operations can complete without waiting for email delivery. The system must track pending notifications and handle delivery failures or delays.


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


