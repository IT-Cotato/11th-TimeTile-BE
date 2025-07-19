FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY build/libs/timetile-0.0.1-SNAPSHOT.jar /app/timetile-server.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=prod", "timetile-server.jar"]