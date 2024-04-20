FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

COPY pom.xml .
COPY bot/pom.xml bot/
COPY bot/src bot/src

COPY scrapper/ scrapper/
COPY shareddto/ shareddto/

COPY mvnw /bot/
COPY .mvn /bot/.mvn

RUN sed -i 's/\r$//' /bot/mvnw
RUN /bot/mvnw install -DskipTests
RUN mkdir -p bot/target/dependency && (cd bot/target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/bot/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","edu.java.bot.BotApplication"]
