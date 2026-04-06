# Multi-stage build: Build stage
FROM maven:3.8.1-openjdk-11-slim as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/service-booking-backend-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=prod"]
