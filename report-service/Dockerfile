FROM maven:3.8.6-openjdk-11 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:resolve

COPY src src
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /reportservice

COPY --from=build /app/target/report-*.jar /reportservice/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]