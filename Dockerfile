FROM eclipse-temurin:21-jdk-jammy AS build
RUN apt-get update
RUN apt-get -y install curl
RUN apt-get -y install gpg
RUN apt-get -y install lsb-release

RUN curl -fsSL https://packages.redis.io/gpg | gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg
RUN echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | tee /etc/apt/sources.list.d/redis.list
RUN apt-get update
RUN apt-get -y install redis

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
