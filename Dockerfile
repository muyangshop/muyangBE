# ── 빌드 단계: Gradle로 실행 가능한 jar 생성 ──
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew bootJar -x test --no-daemon

# ── 실행 단계: JRE만 담아 가볍게 ──
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod
# 호스트가 PORT를 주입 (없으면 8080)
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
