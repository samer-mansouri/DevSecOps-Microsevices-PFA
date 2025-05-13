FROM maven:3.8.6-openjdk-11 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:resolve

COPY src src
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /discoveryserver

COPY --from=build /app/target/*.jar /discoveryserver/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]