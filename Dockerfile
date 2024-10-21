# Stage 1: Build
FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Stage 2: Run
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/car-park-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]