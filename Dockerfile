# Stage 1: Build the JAR file
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Stage 2: Create the Docker image
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar payment-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "payment-api.jar"]