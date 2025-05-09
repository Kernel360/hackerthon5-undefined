# 빌드 스테이지
FROM gradle:8.14-jdk21 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle build --no-daemon

# 실행 스테이지
FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /app/server/build/libs/server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]