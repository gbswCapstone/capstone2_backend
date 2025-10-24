
FROM gradle:8.3.1-jdk17 AS builder
WORKDIR /app


COPY . .


RUN gradle clean build -x test


FROM openjdk:17-jdk-slim
WORKDIR /app


COPY --from=builder /app/build/libs/capstoneProject-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8004


ENTRYPOINT ["java","-jar","app.jar"]
