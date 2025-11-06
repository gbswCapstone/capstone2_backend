
FROM gradle:8.9-jdk21 AS builder
WORKDIR /app


COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x ./gradlew


RUN ./gradlew dependencies --no-daemon || true

COPY . .

RUN ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:21-jdk
WORKDIR /app


COPY --from=builder /app/build/libs/*.jar app.jar

ENV PORT=8004
EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
