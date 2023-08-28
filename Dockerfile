FROM openjdk:17-alpine
LABEL authors="bibliotech"

MAINTAINER bibliotech
COPY target/bibliotech-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]