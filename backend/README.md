# Backend Assignment Scaffold

Spoqa 채용 과제용 Spring Boot(Kotlin) API 서버 스캐폴드입니다.

## 실행

```bash
./gradlew bootRun
```

## 제공 엔드포인트

- `GET /api/v1/health`
- `GET /api/v1/challenge-summary`

## 과제 확장 가이드

- 실제 과제 출제 시 도메인 요구사항에 맞는 Controller/Service/Repository 계층을 추가하세요.
- DB 연동이 필요한 과제라면 `application.yml`과 데이터 접근 계층을 확장하세요.
