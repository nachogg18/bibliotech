FROM openjdk:17-alpine
LABEL authors="bibliotech"

MAINTAINER bibliotech

COPY target/bibliotech-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]