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


