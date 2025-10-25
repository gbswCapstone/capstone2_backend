FROM openjdk:21-jdk-slim


COPY build/libs/*.jar app.jar


RUN ./gradlew clean build -x test

#실행권한추가
RUN chmod +x ./gradlew

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]


EXPOSE 8004


