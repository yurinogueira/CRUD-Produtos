# Build the jar
FROM maven:3-openjdk-17 AS crud-spring-builder

WORKDIR /usr/src/spring

COPY ./spring /usr/src/spring

RUN mvn clean package

# Run the application
FROM openjdk:17-jdk-alpine AS crud-spring

LABEL maintainer="yurinogueira@id.uff.br"
LABEL vendor="Nogueira Tecnologia"

RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

ARG JAR_FILE=/usr/src/spring/target/*.jar

COPY --from=crud-spring-builder ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
