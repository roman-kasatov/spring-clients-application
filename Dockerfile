FROM eclipse-temurin:17-jdk-alpine

COPY ./test-task-0.0.1-SNAPSHOT.jar /opt/service-backend.jar

CMD ["java", "-jar", "/opt/service-backend.jar"]