
FROM gradle:8.9-jdk21 AS builder
WORKDIR /app


COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon || return 0


COPY . .
RUN ./gradlew clean bootJar -x test --no-daemon


FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar


ENV PORT=8004
EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
