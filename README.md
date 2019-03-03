# Software Design - Assignment 1 ![Deadline 24.03.2019 23:59](https://img.shields.io/badge/deadline-24.03.2019%2023%3A59-blue.svg "Deadline: 24.03.2019 23:59")
This is the first assignment of the software design laboratory.

## Resources
The feature descriptions may be found in [this presentation](https://slides.com/spet/utcn-sd-assignment-topic), whilst the theoretical background may be found in [this presentation](https://slides.com/spet/utcn-sd-assignment-1).

## Design Constraints
 * Layered architecture,
 * Using an Abstract factory,
 * Using the repository pattern,
 * With a service layer, 
 * With either transaction script or table module,
 * No U.I. - just a console app.

## Recommended Technologies
 * Spring Boot,
 * JDBC,
 * Hibernate,
 * MySQL or PostgreSQL.

## Getting started
 * Head over to https://start.spring.io/.
 * Leave the defaults on the first line ("Generate a Maven project with Java and Spring Boot 2.x).
 * Group: `ro.utcn.sd.<your initials, nickname, etc>`, example: `ro.utcn.sd.spet`.
 * Artifact: `<project name>`, example: `virtual-classroom`.
 * Dependencies:
   - JDBC,
   - MySQL or PostgreSQL,
   - JPA,
   - Optional: include the Lombok dependency if you have the [Lombok plugin installed](https://projectlombok.org/) in your IDE and you want to write less boilerplate code.
 * Unzip the generated archive contents inside this folder.
 * Open the folder (which should contain the `pom.xml` file) inside IntelliJ IDEA.
 * IntelliJ should download dependencies and you should be able to start coding.
 * Fill in the `application.properties` with your local database connection details: 

```
spring.datasource.url=<<your DB URL>>
spring.datasource.username=<<your DB username>>
spring.datasource.password=<<your DB password>>
```

## Grading

Minimum requirements for a passing grade:
 * Feature 1,
 * In-memory repository,
 * JDBC-backed repository,
 * Abstract factory to select between them,
 * Unit tests for the service layer.

Additional requirements:
| Requirement                        | Grade |
|------------------------------------|-------|
| Hibernate                          |   6   |
| Feature 2                          |   8   |
| Feature 3                          |  10   |

Bonus requirements (for compensating for late delivery or to improve the assignment grade):
| Requirement                        | Points |
|------------------------------------|--------|
| Spring Data Repositories           | 1.0    |
| Database init. with Flyway         | 1.0    |
| Integration tests using H2 DB      | 1.0    |