# UserService

## Summary
`UserService` is a Spring Boot-based microservice designed to handle user-related operations. It provides a robust and scalable solution for registering users, validating their existence, and listing all registered users. The service is built with high performance and efficiency in mind, utilizing **Google Protocol Buffers (Protobuf)** for its data models and API communication, and **PostgreSQL** for persistent storage via **Spring Data JPA**.

## Project Flow
The project follows a standard layered architecture:

1.  **Request Handling Layer:** The `UserController` acts as the primary entry point for all incoming REST requests. It handles data in JSON format but maps them to **Protobuf** messages for internal consistency and efficient serialization.
2.  **Service Layer:** The `UserService` contains the core business logic. It performs:
    *   **Input Validation:** Ensures that the name, email, and ID are non-null and non-empty.
    *   **Duplicate Checking:** Queries the database to ensure that a user with the same email doesn't already exist.
    *   **Orchestration:** Coordinates the conversion between Protobuf models and JPA entities (`UserDao`).
3.  **Data Persistence Layer:** The `UserRepository` (a Spring Data JPA interface) interacts with the **PostgreSQL** database. It provides out-of-the-box methods for common CRUD operations and custom query methods like `findByEmail`.
4.  **Data Models:**
    *   **Protobuf Models:** Define the structure of API requests and responses (e.g., `UserRegisterRequest`, `UserRegisterResponse`).
    *   **JPA Entity (`UserDao`):** Defines the database table structure, including automatic UUID generation for primary keys and audit timestamps (`createdAt`, `updatedAt`).

## API Endpoints
All APIs use `application/json` as the media type.

| Endpoint | Method | Description | Input | Output |
| :--- | :--- | :--- | :--- | :--- |
| `/user/registration` | `POST` | Registers a new user. | `UserRegisterRequest` | `UserRegisterResponse` |
| `/user/validation` | `POST` | Validates if a user exists by ID. | `UserValidationRequest` | `UserValidationResponse` |
| `/users` | `GET` | Retrieves a list of all registered users. | None | `GetUsersResponse` |

### API Details

#### 1. Register User
*   **Path:** `/user/registration`
*   **Method:** `POST`
*   **Input (`UserRegisterRequest`):**
    ```json
    {
      "name": "string",
      "email": "string"
    }
    ```
*   **Output (`UserRegisterResponse`):**
    ```json
    {
      "id": "string (UUID)"
    }
    ```
*   **Exceptions:**
    *   `ValidationException`: If name or email is null or empty.
    *   `DuplicateUserException`: If the email is already registered.
    *   `InternalSystemException`: For unexpected system errors.

#### 2. Validate User
*   **Path:** `/user/validation`
*   **Method:** `POST`
*   **Input (`UserValidationRequest`):**
    ```json
    {
      "id": "string (UUID)"
    }
    ```
*   **Output (`UserValidationResponse`):**
    ```json
    {
      "isValid": boolean
    }
    ```
*   **Exceptions:**
    *   `ValidationException`: If the ID is null or empty.
    *   `InternalSystemException`: For unexpected system errors.

#### 3. Get All Users
*   **Path:** `/users`
*   **Method:** `GET`
*   **Output (`GetUsersResponse`):**
    ```json
    {
      "users": [
        {
          "name": "string",
          "email": "string",
          "id": "string (UUID)"
        }
      ]
    }
    ```
*   **Exceptions:**
    *   `InternalSystemException`: For unexpected system errors.

## Technology Stack
*   **Language:** Java / Kotlin (Build file is Kotlin, source is Java)
*   **Framework:** Spring Boot 4.0.3
*   **Database:** PostgreSQL
*   **Data Access:** Spring Data JPA / Hibernate
*   **Serialization:** Google Protocol Buffers (Protobuf)
*   **Build Tool:** Gradle
*   **Testing:** JUnit 5, Testcontainers (PostgreSQL)
*   **Containerization:** Docker (Dockerfile included)
*   **Orchestration:** Kubernetes (k8s directory included)

## Configuration
Database configuration can be overridden using environment variables:
*   `DB_HOST` (default: `localhost`)
*   `DB_PORT` (default: `5432`)
*   `DB_NAME` (default: `userservice`)
*   `DB_USERNAME` (default: `sneha`)
*   `DB_PASSWORD` (default: `password`)

The application runs on port `8099` by default.

## Database Schema
The service automatically initializes the schema if `spring.sql.init.mode` is set to `always`. The `users` table structure is as follows:
*   `id` (VARCHAR(255), PRIMARY KEY, UUID)
*   `name` (VARCHAR(255))
*   `email` (VARCHAR(50))
*   `createdAt` (TIMESTAMP WITHOUT TIME ZONE)
*   `updatedAt` (TIMESTAMP WITHOUT TIME ZONE)
