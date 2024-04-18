FROM eclipse-temurin:21-jdk-jammy AS build
RUN apt-get update


WORKDIR /workspace/app

COPY ./mvnw .
COPY ./.mvn .mvn
COPY ./pom.xml .
COPY ./src src


RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:21-jre-jammy

RUN adduser --system --group hin
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build --chown=hin:hin ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build --chown=hin:hin ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build --chown=hin:hin ${DEPENDENCY}/BOOT-INF/classes /app
USER hin
CMD ["java","-cp","app:app/lib/*","com.example.test.demo.DemoApplication"]
