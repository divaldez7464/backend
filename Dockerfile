FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy all files from the local directory to the container's /app directory
COPY . .

# Ensure that the pom.xml is present and then run Maven package
RUN mvn clean package -DskipTests

# Use a lightweight base image for the runtime
FROM openjdk:17-jdk-slim

# Copy the built jar file to the runtime image
COPY --from=build /app/target/*.jar /app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Command to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app.jar"]