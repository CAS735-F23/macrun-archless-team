FROM maven:latest AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:latest
COPY --from=build /home/app/target/geo-service-0.0.1-SNAPSHOT.jar /usr/local/lib/geo-service.jar
EXPOSE 9002
ENTRYPOINT ["java","-jar","/usr/local/lib/geo-service.jar"]
