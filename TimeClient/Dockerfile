FROM maven:3-eclipse-temurin-17

WORKDIR /app

COPY ./TimeClient/TimeClient/pom.xml .
RUN mvn dependency:go-offline

COPY ./TimeClient/TimeClient .

RUN mvn package

# RUN mkdir -p /app/compiled_jars
ENTRYPOINT ["cp", "/app/target/TimeClient-1.0-SNAPSHOT-jar-with-dependencies.jar", "/app/compiled_jars/TimeClient.jar"]