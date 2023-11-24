## Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src src
RUN mvn package -DskipTests

# Stage 2: Run
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/karri-go-be.jar karri-go-be.jar
ENTRYPOINT ["java", "-jar", "karri-go-be.jar"]

## define base docker image
#FROM openjdk:21
#
#LABEL maintainer="paschal.net"
#
#ADD target/karri-go-be.jar karri-go-be.jar
#
#ENTRYPOINT ["java", "-jar", "karri-go-be.jar"]



