FROM openjdk:21 as buildstage
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY .env .
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw package -Dmaven.test.skip=true
RUN mv target/*.jar springboot-flash-cards-docker.jar

FROM openjdk:21
RUN ln -s /app/.env .
COPY --from=buildstage /app/springboot-flash-cards-docker.jar .
ENTRYPOINT ["java", "-jar", "springboot-flash-cards-docker.jar"]