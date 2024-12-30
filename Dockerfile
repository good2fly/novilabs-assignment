# Build the project in Docker using maven and Java 21
FROM maven:3.9-eclipse-temurin-21 AS builder
COPY . /build
WORKDIR /build
RUN mvn clean package

# Keep only the resulting uber-jar for the final image
FROM eclipse-temurin:21-jre-alpine
LABEL authors="Attila Uhljar"
COPY --from=builder /build/target/nl-home-assignment-1.0-SNAPSHOT.jar nl-home-assignment.jar
ENTRYPOINT ["java", "-jar", "/nl-home-assignment.jar"]
