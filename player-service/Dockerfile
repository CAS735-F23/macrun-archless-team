FROM maven:latest AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM eclipse-temurin:latest
COPY --from=build /home/app/target/player-service-0.0.1-SNAPSHOT.jar /usr/local/lib/player-service.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar","/usr/local/lib/player-service.jar"]
