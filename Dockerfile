FROM eclipse-temurin:17

ADD target/karri-go-be.jar karrigo.jar

ENTRYPOINT ["java", "-jar","karrigo.jar"]
