# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY app/target/rates-service.jar /app/rates-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/rates-service.jar"]
