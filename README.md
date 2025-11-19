# Secure Spring Boot Application with JWT and OAuth2

This project is a comprehensive demonstration of a secure Spring Boot application that leverages Spring Security to provide robust authentication and authorization. It includes both traditional JWT-based authentication and social login with OAuth2 providers like Google and GitHub. The application is also fully instrumented for observability with the ELK stack for logging and Prometheus/Grafana for metrics monitoring.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Local Setup](#local-setup)
  - [Running with Docker](#running-with-docker)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
  - [Application Properties](#application-properties)
  - [OAuth2 Configuration](#oauth2-configuration)
- [Observability](#observability)
  - [ELK Stack](#elk-stack)
  - [Prometheus and Grafana](#prometheus-and-grafana)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Authentication:**
  - **JWT-based Authentication:** Secure sign-in and sign-up with JSON Web Tokens.
  - **OAuth2 Social Login:** Support for social login with Google and GitHub.
- **Authorization:**
  - **Role-Based Access Control (RBAC):** Secure endpoints based on user roles (e.g., `USER`, `ADMIN`).
  - **Secured Endpoints:** H2 database console and Swagger UI are restricted to `ADMIN` users.
- **Observability:**
  - **Centralized Logging (ELK Stack):** The application is configured to send logs to a Logstash instance, which then forwards them to Elasticsearch for storage and analysis in Kibana.
  - **Metrics Monitoring (Prometheus & Grafana):** The application exposes metrics through the Actuator endpoint, which can be scraped by Prometheus and visualized in Grafana.
- **API Documentation:**
  - **Swagger/OpenAPI:** Integrated with SpringDoc to provide interactive API documentation.
- **Testing:**
  - **Unit & Integration Tests:** Comprehensive test coverage for services, controllers, and security configurations.
  - **Test Profiles:** Support for different testing environments (e.g., `dev`, `test`, `stage`, `prod`).

## Architecture

The application follows a standard layered architecture:

- **Controller Layer:** Exposes the REST APIs for authentication and other resources.
- **Service Layer:** Contains the business logic for user management, authentication, and other operations.
- **Repository Layer:** Handles data access and persistence using Spring Data JPA.
- **Security Layer:** Implements the security configuration, including JWT and OAuth2 support, using Spring Security.

## Technologies Used

- **Framework:** Spring Boot 3
- **Language:** Java 21
- **Security:** Spring Security, JWT, OAuth2
- **Database:** H2 (for testing), PostgreSQL (for production)
- **API Documentation:** SpringDoc (Swagger/OpenAPI)
- **Logging:** Logstash, Elasticsearch, Kibana (ELK Stack)
- **Metrics:** Prometheus, Grafana
- **Build Tool:** Maven

## Prerequisites

- Java 21
- Maven 3.6+
- Docker and Docker Compose

## Getting Started

### Local Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/secure-app.git
    cd secure-app
    ```

2.  **Configure OAuth2:**
    - Open `src/main/resources/application.properties`.
    - Replace the placeholder values for `YOUR_GOOGLE_CLIENT_ID`, `YOUR_GOOGLE_CLIENT_SECRET`, `YOUR_GITHUB_CLIENT_ID`, and `YOUR_GITHUB_CLIENT_SECRET` with your actual OAuth2 client credentials.

3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```

The application will be available at `http://localhost:8080`.

### Running with Docker

This project includes Docker Compose configurations for running the observability stack.

#### ELK Stack

To run the ELK stack (Elasticsearch, Logstash, Kibana):

1.  **Navigate to the ELK directory:**
    ```bash
    cd docker/elk
    ```

2.  **Start the services:**
    ```bash
    docker-compose up -d
    ```

- **Kibana:** `http://localhost:5601`
- **Logstash:** Listens on TCP port `5000`
- **Elasticsearch:** `http://localhost:9200`

#### Prometheus and Grafana

To run the monitoring stack (Prometheus, Grafana):

1.  **Navigate to the monitoring directory:**
    ```bash
    cd docker/monitoring
    ```

2.  **Start the services:**
    ```bash
    docker-compose up -d
    ```

- **Grafana:** `http://localhost:3000` (login with `admin`/`admin`)
- **Prometheus:** `http://localhost:9090`

**Note for Linux users:** The Prometheus configuration uses `host.docker.internal` to connect to the Spring Boot application running on the host. If you are on Linux, you may need to add the following to your `docker-compose.yml` file for the Prometheus service:
```yaml
extra_hosts:
  - "host.docker.internal:host-gateway"
```

## API Endpoints

### Authentication

- `POST /api/auth/signup`: Register a new user.
- `POST /api/auth/signin`: Sign in an existing user and receive a JWT token.
- `GET /oauth2/authorization/{provider}`: Initiate the OAuth2 login flow (e.g., `/oauth2/authorization/google`).

### Test Endpoints

- `GET /api/test/user`: A secured endpoint accessible to all authenticated users.
- `GET /api/test/admin`: A secured endpoint accessible only to users with the `ADMIN` role.

### API Documentation (Swagger)

The Swagger UI is available at `http://localhost:8080/swagger-ui.html` and is accessible only to `ADMIN` users.

## Configuration

### Application Properties

The main configuration file is `src/main/resources/application.properties`. It contains properties for:

- **JWT:** `app.jwt-secret`, `app.jwt-expiration-milliseconds`
- **OAuth2:** `spring.security.oauth2.client.registration.*`
- **Database:** `spring.datasource.*` (for different profiles)

### OAuth2 Configuration

To enable social login, you must create OAuth2 applications with your chosen providers (e.g., Google, GitHub) and configure the client ID and client secret in the `application.properties` file.

## Observability

### ELK Stack

The application sends logs to Logstash, which are then indexed in Elasticsearch. You can view and analyze the logs in Kibana at `http://localhost:5601`.

### Prometheus and Grafana

The application exposes metrics at `/actuator/prometheus`. Prometheus scrapes these metrics, and you can visualize them in Grafana at `http://localhost:3000`. A pre-configured JVM dashboard is included.

## Testing

To run the tests:

```bash
./mvnw test
```

The tests are configured to run with the `test` profile, which uses an in-memory H2 database.

## Contributing

Contributions are welcome! Please feel free to submit a pull request.

## License

This project is licensed under the MIT License.