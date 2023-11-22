## Stage 1: Build
#FROM maven:3.8.5-openjdk-17 AS build
#WORKDIR /app
#COPY pom.xml .
#RUN mvn dependency:resolve
#COPY src src
#RUN mvn package -DskipTests
#
## Stage 2: Run
#FROM openjdk:17
#WORKDIR /app
#COPY --from=build /app/target/karri-go-be.jar karrigo.jar
#ENTRYPOINT ["java", "-jar", "karrigo.jar"]

# Stage 1: Build Java
FROM maven:3.8.5-openjdk-17 AS build-java
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src src
RUN mvn package -DskipTests

# Stage 2: Build Go
FROM golang:1.17 AS build-go
WORKDIR /app
COPY . .
RUN go build -o server ./cmd/app

# Final Stage
FROM openjdk:17
WORKDIR /app
COPY --from=build-java /app/target/karri-go-be.jar karrigo.jar
COPY --from=build-go /app/server .
ENTRYPOINT ["java", "-jar", "karrigo.jar"]




