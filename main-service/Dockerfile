FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/main-service-0.0.1-SNAPSHOT.jar /app/main-service.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "main-service.jar"]