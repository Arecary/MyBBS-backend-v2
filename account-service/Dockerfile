FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/account-service-0.0.1-SNAPSHOT.jar /app/account-service.jar
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "account-service.jar"]