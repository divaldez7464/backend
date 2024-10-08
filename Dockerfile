FROM maven:3.8.5-openjdk-17 AS build

# Set working directory
WORKDIR /app

# First, copy only the pom.xml file
COPY pom.xml .

# Run mvn dependency resolve (this will cache the downloaded dependencies)
RUN mvn dependency:go-offline

# Now copy the rest of the application source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Second stage, use a lightweight JDK image for the runtime
FROM openjdk:17-jdk-slim

# Copy the built jar from the Maven build stage
COPY --from=build /app/target/*.jar /app.jar

# Expose the port that the Spring Boot app will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]