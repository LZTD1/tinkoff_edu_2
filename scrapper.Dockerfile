FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

COPY pom.xml .
COPY scrapper/pom.xml scrapper/
COPY scrapper/src scrapper/src

COPY bot/ bot/
COPY shareddto/ shareddto/

COPY mvnw /scrapper/
COPY .mvn /scrapper/.mvn

RUN sed -i 's/\r$//' /scrapper/mvnw
RUN /scrapper/mvnw install -DskipTests
RUN mkdir -p scrapper/target/dependency && (cd scrapper/target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/scrapper/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","edu.java.ScrapperApplication"]
