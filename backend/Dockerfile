# ---------------------------------------------------------------------------
# 멀티 스테이지: JDK로 fat JAR 생성 후, JRE만 있는 런타임 이미지로 축소합니다.
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x ./gradlew \
  && ./gradlew bootJar --no-daemon -x test

# ---------------------------------------------------------------------------
# 런타임: 실행에 필요한 JRE와 JAR만 포함합니다.
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
