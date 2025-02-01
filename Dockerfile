FROM openjdk:17
WORKDIR /app
COPY target/*.jar payment-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "payment-api.jar"]