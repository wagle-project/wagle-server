# 🤖 Wagle-Server AI Agent Guide (CLAUDE.md)

이 문서는 AI 에이전트(Claude Code, Antigravity 등)가 `Wagle-Server` 프로젝트를 다룰 때 최우선으로 숙지해야 하는 특급 가이드라인(Core Rules)입니다.

## 🏛️ 프로젝트 필수 아키텍처
- **언어 & 프레임워크:** Java 21, Spring Boot
- **데이터베이스:** MySQL 8.0, Redis (Docker Compose 베이스)
- **빌드 도구:** Gradle
- **CI/CD 핵심 포인트:** 
  - GitHub Actions 기반 빌드 파이프라인
  - n8n + Antigravity를 활용해 빌드 실패 및 서버 구동 중 Crash 발생 시 **자가 치유(Self-Healing)** 대응 진행 (`trigger-ai-fix.sh`, `start-app.sh` 구동)

## 🛠️ AI 에이전트의 문제 해결 행동 강령
서버 오류로 인해 터미널에서 백그라운드로 소환될 경우, 에이전트는 결코 당황하지 말고 아래 절차를 기계적으로 수행합니다:

1. **에러 원인 추적:** 터미널에 전달된 핵심 로그를 바탕으로 Java 로직이나 설정 파일(`application.yml`, Docker)에서 에러 지점 색출
2. **최소 변경 수정:** 프로젝트의 뼈대(@ControllerAdvice, 아키텍처 패턴 등)를 임의로 갈아엎지 말고 핀포인트로만 얌전하게 Fix
3. **가상 로컬 컴파일 검증:** 수정을 마쳤으면 터미널에서 `./gradlew clean build -x test` 를 돌려 빨간 줄이 없는지 테스트
4. **Git 워크플로우 준수:** 
   - 반드시 오류 수정용 새 브랜치(`auto-fix/...`) 생성 후 작업
   - 원격지(origin) Push 후 `gh pr create` 명령어로 개발자에게 원본 브랜치 PR(Pull Request) 상신
5. **Notion 자동 문서화:** 
   - PR까지 올렸으면 반드시 노션 데이터베이스(`328b0540-790a-80bc-99a4-e7f047e5373a`)의 하위에 트러블슈팅 정리 노트 작성
6. **안전장치 (인프라 에러):** 인프라 장애(포트 죽음, 비밀번호 누락 등)로 코드 수정을 통한 진압이 불가능할 경우 즉시 `MANUAL_INTERVENTION_REQUIRED` 문자열을 출력하여 개발자 소환할 것.
