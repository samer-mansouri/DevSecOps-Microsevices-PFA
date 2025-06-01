FROM maven:3.8.6-openjdk-11 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:resolve

COPY src src
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /userservice

COPY --from=build /app/target/*.jar /userservice/app.jar

ENV JAVA_OPTS="-Djavax.net.ssl.trustStore=/truststore/truststore.jks \
-Djavax.net.ssl.trustStorePassword=changeit"

COPY truststore.jks /truststore/truststore.jks

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]