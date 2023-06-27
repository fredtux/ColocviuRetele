FROM maven:3-eclipse-temurin-17

WORKDIR /app

COPY ./TimeServer/TimeServer/pom.xml .
RUN mvn dependency:go-offline

COPY ./TimeServer/TimeServer .

RUN mvn package

ENTRYPOINT ["java", "-jar", "target/TimeServer-1.0-SNAPSHOT-jar-with-dependencies.jar"]