# Syscode homework

## Prerequisites

Before you begin, ensure you have met the following requirements:

- [Java Development Kit (JDK) 11](https://adoptopenjdk.net/)
- [Apache Maven](https://maven.apache.org/)

## Getting Started

Follow these steps to set up and run the application.

1. **Build the application**
   ```bash
   mvn clean install
   ```
   
   This will run the unit tests as well as the integration test.
   
   To run the unit tests without rebuilding, run:
   ```bash
   mvn test
   ```
   
   To run the unit tests and the integration test without rebuilding, run:
   ```bash
   mvn verify
   ```

2. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   
   You can access the application at http://localhost:8080
   
   If you wish to take a look at available APIs, check http://localhost:8080/swagger-ui/index.html
   
   You can import the included Postman collection from the root folder.