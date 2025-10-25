FROM openjdk:21-jdk-slim



WORKDIR /app


COPY . .


RUN chmod +x ./gradlew


RUN ./gradlew clean build -x test


COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]


EXPOSE 8004


