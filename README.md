# Software Testing Course Projects

## Introduction

The course assignments focus on applying various software testing techniques to **Mizdooni** (a Spring Boot web application for restaurant reservations and reviews) and **Fesadyab** (a Java-based fraud detection system). Throughout the series of computer assignments (CAs), practical implementation tasks, ranging from unit and mock testing to API, graph coverage, BDD, and mutation testing, are combined with theoretical analysis covered in the project reports.

## Assignments

### CA1 (JUnit)
Unit tests were written for the `User`, `Table`, `Restaurant`, and `Rating` models.<br>
The 4 unit test files can be found in `Mizdooni/src/test/java/mizdooni/model`.<br>
Some tests make use of parameterized testing.<br>
The explanatory questions are about `AssumeTrue`, multi-threaded testing, output printing vs. assertions, and identifying problems in sample test code.


### CA2 (Mock Testing)

Tests were written for the `AuthenticationController`, `ReservationController`, and `ReviewController` classes.<br>
The 3 controller test files can be found in `Mizdooni/src/test/java/mizdooni/controllers`.<br>
The tests mock the Mizdooni service classes using the **Mockito** framework.<br>
The explanatory questions are about state vs. behavior verification, test spies, and shared fixture strategies.



### CA3 (Graph Coverage)

Tests were written to evaluate maximum coverage for the Fesadyab system in the `domain` package.<br>
The test files are located in `Fesadyab/src/test/java/domain`.<br>
The tests aim to achieve maximum branch and statement coverage (minimum 90%) using the **JaCoCo** library.<br>
The explanatory questions are about drawing Control Flow Graphs (CFGs), identifying Prime and DU Paths, determining required test cases for 100% statement and branch coverage, and proving path coverage relationships.

### CA4 (API Testing)

API tests were written for the `TableController` and `RestaurantController` classes.<br>
The 2 controller test files can be found in `Mizdooni/src/test/java/mizdooni/controllers`.<br>
The tests use `@SpringBootTest` or `@WebMvcTest` with **MockMvc** to send Web API requests and validate the responses.<br>
The explanatory questions are about `@WebMvcTest` vs. `@SpringBootTest`, logic-based coverage criteria (CACC/RACC), and input space partitioning with pair-wise testing.


### CA5 (Mutation Testing & CI/CD Pipeline)

Unit tests were written for the `Transaction` and `TransactionEngine` classes in the Fesadyab system to calculate mutation coverage using the **PIT** tool.<br>
The test suite can be found in `Fesadyab/src/test/java`.<br>
The mutation coverage results (including total created, killed, and surviving mutants) are analyzed in the report, alongside an explanation of the impact of high mutation coverage on refactoring safety.<br>
A **GitHub Actions** workflow (`.github/workflows`) was also created to automate the continuous integration pipeline that builds the project and executes tests on push events.


### CA6 (Behavior Driven Development & GUI Testing)

Behavior-driven tests were written for the `addReservation` method in the `User` class, as well as the `getAverageRating` and `addReview` methods in the `Restaurant` class.<br>
Feature files are located in `Mizdooni/src/test/resources`.<br>
The BDD scenarios are written in Gherkin and run using the **Cucumber** framework.<br>
Recorded GUI testing was also performed on **Swagger UI** endpoints for Mizdooni using **Katalon Recorder** (exported as JUnit Java WebDriver scripts).