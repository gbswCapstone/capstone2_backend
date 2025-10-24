# jdk21 이미지
FROM eclipse-temurin:21-jdk-jammy

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 빌드 후 jar 파일 복사
# 빌드된 jar 파일 이름이 capstoneProject.jar
#COPY build/libs/capstoneProject.jar app.jar

#COPY ${JAR_FILE} app.jar

ARG JAR_FILE=build/libs/capstoneProject-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8004

# -Dspring.profiles.active=prod 는 필요시 활성화
ENTRYPOINT ["java","-jar","/app/app.jar"]