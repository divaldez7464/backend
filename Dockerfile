# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper files and build.gradle
COPY gradlew ./
COPY gradlew.bat ./
COPY build.gradle ./
COPY settings.gradle ./

# Copy the source code into the container
COPY src ./src

# Ensure that the Gradle wrapper has executable permissions
RUN chmod +x gradlew

# Build the project (excluding tests)
RUN ./gradlew build -x test

# Expose the application port (adjust as needed, typically 8080 for Spring Boot)
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "build/libs/backend.jar"]