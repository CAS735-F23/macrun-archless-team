FROM maven:latest AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM eclipse-temurin:latest
COPY --from=build /home/app/target/challenge-service-0.0.1-SNAPSHOT.jar /usr/local/lib/challenge-service.jar
EXPOSE 9001
ENTRYPOINT ["java","-jar","/usr/local/lib/challenge-service.jar"]