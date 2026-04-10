# UserService

## Summary
`UserService` is a Spring Boot-based microservice designed to manage user-related data. It provides a robust and scalable solution for registering new users, validating user existence by ID, and retrieving a complete list of registered users. The service is optimized for performance by using **Google Protocol Buffers (Protobuf)** for its API communication layer and **PostgreSQL** for persistent storage, integrated via **Spring Data JPA**. It also includes built-in observability with **Micrometer** for metrics tracking (Timed and Counted aspects).

## Project Flow
The project follows a standard layered architecture to ensure separation of concerns and maintainability:

1.  **Request Layer (`UserController`):**
    *   Acts as the entry point for RESTful requests.
    *   Uses `ProtobufJsonFormatHttpMessageConverter` to handle JSON-Protobuf conversion.
    *   Annotated with `@Timed` and `@Counted` for performance monitoring and request counting.
2.  **Service Layer (`UserService`):**
    *   Contains the core business logic.
    *   **Validation:** Ensures that `name`, `email`, and `id` are provided and non-empty.
    *   **Duplicate Check:** Queries the database to prevent duplicate registrations with the same email.
    *   **Orchestration:** Manages the lifecycle of user data, including mapping between Protobuf messages (`User`, `UserRegisterRequest`) and JPA entities (`UserDao`).
3.  **Persistence Layer (`UserRepository`):**
    *   Leverages Spring Data JPA for interacting with the **PostgreSQL** database.
    *   Handles CRUD operations and custom queries like `findByEmail`.
4.  **Data Models:**
    *   **Protobuf (`user_service.proto`):** Defines the contract for external communication.
    *   **JPA Entity (`UserDao`):** Defines the database schema, including automatic UUID generation and audit timestamps (`createdAt`, `updatedAt`).

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
*   **Key Logic:**
    *   Validates that `name` and `email` are not empty.
    *   Checks if a user with the same `email` already exists.
    *   Generates a unique `id` (UUID) upon successful registration.

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
*   **Key Logic:**
    *   Validates that `id` is not empty.
    *   Returns `true` if a user exists with the given ID, `false` otherwise.

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
*   **Key Logic:**
    *   Fetches all user records from the database and maps them to the Protobuf `User` model.

## Technology Stack
*   **Framework:** Spring Boot 4.0.3
*   **Database:** PostgreSQL
*   **Communication:** REST with Protobuf (via Jackson Protobuf module)
*   **Data Access:** Spring Data JPA / Hibernate
*   **Observability:** Micrometer (Prometheus integration)
*   **Build Tool:** Gradle (Kotlin DSL)
*   **Language:** Java (Source) / Kotlin (Build scripts)
*   **Infrastructure:** Docker, Kubernetes (k8s manifests included)

## Database Schema
The service interacts with a `users` table with the following structure:
*   `id` (VARCHAR, Primary Key): Automatically generated UUID.
*   `name` (VARCHAR): User's name.
*   `email` (VARCHAR): User's unique email address.
*   `createdat` (TIMESTAMP): Automatic timestamp on record creation.
*   `updatedat` (TIMESTAMP): Automatic timestamp on record update.

## Configuration & Deployment
*   **Port:** 8099 (Default)
*   **Database Config:** Customizable via environment variables (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`).
*   **Observability:** Exposes metrics for Prometheus via `/actuator/prometheus` (if enabled).
*   **Docker:** Use the provided `Dockerfile` to build the container image.
*   **Kubernetes:** Deployment and Service manifests are available in the `k8s` directory.
