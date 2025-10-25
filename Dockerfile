#FROM openjdk:21-jdk-slim AS builder
#WORKDIR /app
#
#
#COPY . .
#RUN chmod +x ./gradlew
#
#
#RUN ./gradlew clean build -x test
#
#
#FROM openjdk:21-jdk-slim
#WORKDIR /app
#
#
#COPY --from=builder /app/build/libs/capstoneProject-0.0.1-SNAPSHOT.jar app.jar
#
#
#ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
#
#
#EXPOSE 8004
