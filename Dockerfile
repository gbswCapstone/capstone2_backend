# Java 21을 사용하는 이미지
FROM openjdk:21-jdk-slim

# 빌드한 jar파일을 컨테이너에 복사
COPY build/libs/*.jar app.jar


EXPOSE 8004

# 컨테이너가 시작되면 실행할 명령어
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]