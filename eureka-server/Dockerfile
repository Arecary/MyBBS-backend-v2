FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/eureka-server-0.0.1-SNAPSHOT.jar /app/eureka-server.jar
EXPOSE 10086
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]