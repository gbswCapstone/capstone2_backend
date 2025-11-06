#FROM gradle:8.9-jdk21 AS builder
#WORKDIR /app
#
#COPY capstoneProject/ ./
##RUN chmod +x ./gradlew
##RUN ./gradlew clean bootJar -x test --no-daemon
#
#FROM openjdk:21-jdk-slim
##WORKDIR /app
#COPY build/libs/*.jar app.jar
#ENV PORT=8004
#EXPOSE ${PORT}
#
#ENTRYPOINT ["java", "-jar", "app.jar"]


# Java 21을 사용하는 이미지
FROM openjdk:21-jdk-slim

# 빌드한 jar파일을 컨테이너에 복사
COPY build/libs/*.jar app.jar

# 도커에게 컨테이너가 8080 포트를 외부에 노출할 것이라고 알려주는 명령어
EXPOSE 8004

# 컨테이너가 시작되면 실행할 명령어
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]