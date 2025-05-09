# 빌드 스테이지
FROM gradle:8.14-jdk21 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle clean build -x test
RUN pwd

# 실행 스테이지
FROM amazoncorretto:21
WORKDIR /app
RUN PWD
RUN ls
COPY --from=builder build/libs/server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]