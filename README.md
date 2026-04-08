# Practice Assignment: Items Service

## Overview
A microservice built with Java 21 and Spring Boot 3.4.1 to manage items and publish lifecycle events via Apache Kafka.

## Architectural Decisions & Rationale
The architecture prioritizes resilience, thread safety, and decoupling over strict consistency. An in-memory `ConcurrentHashMap` acts as a thread-safe persistence layer, while idempotency is enforced at the service level (HTTP 409) to prevent state duplication. Furthermore, the Kafka integration is designed for high availability; the producer gracefully handles broker unreachability to ensure the core business transaction completes even during messaging infrastructure degradation.

**Additional Notes:**
- **Persistence**: Per requirements, wrapped in a Repository pattern to ensure separation of concerns.
- **Messaging Format**: Strictly adheres to the requested format: `"hello world: <itemId>"`.
- **Infrastructure**: The project uses **Testcontainers** for seamless, automated infrastructure provisioning during development and testing.

## Prerequisites & Configuration

### 1. Environment Requirements
- Java 21 JDK installed
- Docker Desktop or Colima (Must be actively running)

### 2. Maven Configuration
Before building the project, you must configure a custom Maven settings file to support the Spotless plugin for code formatting.

Create a file at `~/.m2/settings-joselazo.xml` with the following content:

```
xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xsi:schemaLocation="[http://maven.apache.org/SETTINGS/1.2.0](http://maven.apache.org/SETTINGS/1.2.0) [https://maven.apache.org/xsd/settings-1.2.0.xsd](https://maven.apache.org/xsd/settings-1.2.0.xsd)" xmlns="[http://maven.apache.org/SETTINGS/1.2.0](http://maven.apache.org/SETTINGS/1.2.0)"
    xmlns:xsi="[http://www.w3.org/2001/XMLSchema-instance](http://www.w3.org/2001/XMLSchema-instance)">
  <pluginGroups>
    <pluginGroup>com.diffplug.spotless</pluginGroup>
  </pluginGroups>
</settings>
```
### 3. Build Lifecycle
Execute the following commands sequentially from the project root directory. These commands utilize the custom settings profile we created to compile, test, and package the application.

**Note:** Ensure your Docker daemon is active, as the test and verify phases require Testcontainers to spin up a temporary Kafka broker.
```Bash
mvn clean test -s ~/.m2/settings-joselazo.xml
mvn clean compile -s ~/.m2/settings-joselazo.xml
mvn clean install -s ~/.m2/settings-joselazo.xml
mvn clean verify -s ~/.m2/settings-joselazo.xml
mvn clean package -s ~/.m2/settings-joselazo.xml
```

### 4. Running the Application
There are two primary methods to start the application, depending on your infrastructure requirements.

**Auto-Provisioned Kafka:** For development mode, execute: `TestItemsApplication.java` (located in `src/test/java/com/joselazotest/items/`)
**How to run:** Execute the main method directly from your IDE.
**Why it is necessary:** This approach leverages Spring Boot Testcontainers (`@ServiceConnection`). It automatically downloads and starts a Dockerized Apache Kafka broker, injecting the connection properties into the application dynamically. It is the required method for local development without a manual Kafka installation.

### 5. Verification & Observability
Once the application has successfully started, open the following URLs in your web browser to verify the system's health and explore the API documentation:

**System Health (Actuator):** `http://localhost:8080/actuator/health` which verifies that the application is running and that components (like disk space and Kafka connections) are operational.
**API Documentation (Swagger UI):** `http://localhost:8080/swagger-ui/index.html` that provides an interactive OpenAPI 3.0 interface. You can use this to review the data schemas, test the POST /items endpoint, and analyze the structured error responses.

