FROM maven:3-eclipse-temurin-17

WORKDIR /app

COPY ./dns/DNS/pom.xml .
RUN mvn dependency:go-offline

COPY ./dns/DNS .

RUN mvn package

ENTRYPOINT ["java", "-jar", "target/DNS-1.0-SNAPSHOT-jar-with-dependencies.jar"]