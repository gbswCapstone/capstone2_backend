FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app


COPY . .


RUN ./gradlew clean build -x test

#실행권한추가
RUN chmod +x ./gradlew

ENTRYPOINT ["java","-jar","build/libs/capstoneProject-0.0.1-SNAPSHOT.jar"]
EXPOSE 8004
