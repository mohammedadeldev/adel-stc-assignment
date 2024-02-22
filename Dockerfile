# Stage 1: Build the application using a slim Maven image
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src src
# Compile the application, skipping tests to save time
RUN mvn clean package -DskipTests

# Stage 2: Package the application in a slim Java runtime image
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
