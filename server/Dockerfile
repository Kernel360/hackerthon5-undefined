FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /app/server/build/libs/server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]