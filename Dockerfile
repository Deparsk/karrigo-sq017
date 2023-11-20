#FROM eclipse-temurin:17
#
#ADD target/karri-go-be.jar karrigo.jar
#
#ENTRYPOINT ["java", "-jar","karrigo.jar"]

#FROM maven:3.8.5-openjdk-17 AS build
#COPY . .
#RUN mvn clean package -DskipTests
#
#FROM openjdk:17.0.1-jdk-slim
#COPY --from=build /target/karriGo-java017-BE-develop-0.0.1-SNAPSHOT.jar karriGo-java017-BE-develop.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "karriGo-java017-BE-develop"]

FROM openjdk:17

LABEL maintainer="paschal.com"

ADD target/karri-go-be.jar karrigo.jar

ENTRYPOINT ["java", "-jar", "karrigo.jar"]
