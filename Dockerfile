FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY build.gradle ./
COPY settings.gradle ./
COPY src ./src
RUN ./gradlew build -x test
CMD ["java", "-jar", "build/libs/your-app.jar"]