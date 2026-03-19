From alpine/java:17-jdk AS build

COPY . .

RUN ./gradlew clean build -x test

FROM alpine/java:21-jre

COPY --from=build /build/libs/*-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]