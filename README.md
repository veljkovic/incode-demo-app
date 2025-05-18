# Spring Boot Incode Demo Project

A Spring Boot application that demonstrates REST API development with PostgreSQL database integration.

## Technologies Used

- Java 21
- Spring Boot 3.4.5
- PostgreSQL
- Maven
- Docker & Docker Compose

## Prerequisites

- Java 21 or higher
- Maven
- Docker and Docker Compose (for containerized deployment)
- PostgreSQL (if running locally)

## Getting Started

### Running with Docker

1. Clone the repository:
```bash
git clone https://github.com/veljkovic/incode-demo-app.git
cd demo
```

2. Start the application using Docker Compose:
```bash
docker-compose up --build
```

The application will be available at `http://localhost:8080`
OpenAPI documentation is available at `http://localhost:8080/swagger-ui/index.html#/`

### Running Locally

1. Set up environment variables:
```bash
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
```

2. Run the application:
```bash
./mvnw spring-boot:run
```

### Running Tests

To run the test suite:
```bash
mvn test
```