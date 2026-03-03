# 1. 빌드 스테이지: JDK 21 기반
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# 종속성 파일을 먼저 복사하여 캐시 효율 증대
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 실행 권한 부여 및 의존성 미리 다운로드
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사 및 jar 빌드 (테스트 제외)
COPY src src
RUN ./gradlew clean build -x test --no-daemon

# 2. 실행 스테이지: JRE 21 기반 (용량 최적화)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 빌드 결과물 중 0.0.1-SNAPSHOT.jar 파일만 app.jar로 복사
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

# 컨테이너 포트 설정
EXPOSE 8080

# 배포 환경 설정 및 한국 시간대 설정 후 실행
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]