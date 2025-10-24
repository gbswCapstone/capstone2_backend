
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

RUN apt-get update && apt-get install -y curl unzip \
    && curl -L https://services.gradle.org/distributions/gradle-8.3.1-bin.zip -o gradle.zip \
    && unzip gradle.zip -d /opt/ \
    && rm gradle.zip
ENV PATH="/opt/gradle-8.3.1/bin:${PATH}"


COPY . .


RUN gradle clean build -x test


ENTRYPOINT ["java","-jar","build/libs/capstoneProject-0.0.1-SNAPSHOT.jar"]
EXPOSE 8004
