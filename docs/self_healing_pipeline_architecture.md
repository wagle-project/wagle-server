# 🤖 Wagle-Server 자율 치유(Auto-Healing) 파이프라인 아키텍처

이 문서는 wagle-server 프로젝트에 구축된 **Zero-Touch CI/CD 및 런타임 자율 복구 파이프라인**의 전체 동작 원리에 대해 다른 AI(또는 팀원)에게 설명하기 위한 기술 요약본입니다.

---

## 🏗️ 1. 전체 아키텍처 개요
본 파이프라인은 서버가 크래시 나거나 GitHub 빌드가 실패했을 때, 인간의 개입(Human-in-the-loop) 없이 **AI 에이전트(Aider)가 직접 에러를 분석하고 코드를 수정하여 커밋까지 완료**하는 완전 자동화 루프입니다.

- **Trigger:** GitHub Actions (CI) / Spring Boot Runtime (서버)
- **Middleware:** ngrok -> n8n (웹훅 라우팅 및 알림 관리)
- **Agent:** Aider (CLI 기반 자율 코딩 에이전트, `gemini-2.5-flash` 모델 사용)
- **Notification:** Discord

---

## ⚙️ 2. 핵심 컴포넌트 및 동작 흐름

### A. 에러 감지 및 트리거 (2-Way)
1. **GitHub CI/CD 빌드 실패 (빌드, 테스트 에러)**
   - GitHub Actions가 실패하면 Github Webhook이 n8n(`POST /webhook/github-ci-event`)으로 Payload 전송.
2. **Spring Boot 런타임 크래시 (`start-app.sh`)**
   - 개발/운영 서버 부팅 시 JVM이 터지면, 쉘 스크립트가 `EXIT_CODE != 0`을 가로챔.
   - 가장 핵심적인 마지막 로그 200줄을 `jq`로 파싱하여 n8n(`POST /webhook/server-error-event`)으로 직송.

### B. 미들웨어 라우팅 (n8n JSON Workflow)
- n8n이 트리거를 수신하고, 실패 조건(`conclusion == failure` 등)을 필터링합니다.
- 담당자 디스코드 채널에 **"🚨 자동 복구를 시작합니다"** 알림을 발송.
- 백그라운드에서 `n8n-nodes-base.executeCommand` 노드를 통해 로컬 쉘 스크립트(`trigger-ai-fix.sh`) 실행 권한 위임.

### C. 자율 복구 처리 (`trigger-ai-fix.sh` + Aider)
가장 핵심적인 뇌 역할을 하는 스크립트입니다. n8n 데몬의 제약을 극복하고 다음과 같이 작동합니다:
1. **에러 텍스트 파싱 분기:**
   - *GitHub 트리거*라면 `$RUN_ID`를 받아 `gh run view $RUN_ID --log-failed` 명령어로 실제 스택 프레이스를 뽑아옴.
   - *서버 직송 트리거*라면 `$RUN_ID`가 `SERVER_FATAL*` 인 것을 인식하고 넘어온 파라미터 `$4`를 그대로 사용.
2. **보안 환경 구축:**
   - `.env` 파일명시 로드로 `ANTHROPIC_API_KEY` 혹은 `GEMINI_API_KEY` 환경변수 주입.
3. **Headless AI Agent 실행:**
   - `aider --model gemini/gemini-2.5-flash --yes --message {PROMPT}` 로 대화형 터미널(TTY) 없이 백그라운드 강제 구동.
   - Aider가 에러 문맥(Prompt)을 읽고 로컬 리포지토리의 코드 파일 컨텍스트를 분석.
   - 파일을 수정(Self-patching) 후 Git Commit까지 완료.

---

## 🔑 3. 타 AI가 참고해야 할 개발/유지보수 포인트
다음 AI 담당자가 파이프라인을 유지 보수할 때 반드시 인지해야 할 주요 제약 및 주의사항입니다.

1. **상태를 가지지 않는(vanilla) 쉘 주의:**
   - n8n 백그라운드 실행 시 사용자의 `.zshrc` 등을 절대 상속받지 않습니다. 경로(`/opt/homebrew/bin/aider` 등) 명시 및 환경변수는 스크립트 안에 하드코딩된 로직으로 주입되어야 합니다.
2. **비대화형(Headless) 모드 제약:**
   - 일반 CLI 도구는 화면(TTY)이 없으면 즉시 종료되므로, Aider 명령어에 `--yes` 및 `--message` 같은 터미널 우회(Headless) 옵션이 영원히 유지되어야 합니다.
   - `stdout` 이나 `stderr` 가 n8n 로그로 빠지며 에이전트 인터랙션이 불가능하므로, `trigger-ai-fix.sh` 안의 모든 동작은 **알아서 고치고 Exit 0**으로 끝나야 합니다.
3. **보안 주의:**
   - 깃 허브 리포지토리에 추적(Track)되는 스크립트 파일이므로, `trigger-ai-fix.sh` 내부에서 API KEY를 직접 명시하지 않고 반드시 무시된(`.env`) 환경 변수 파일을 로드하는 체계를 훼손시켜선 안 됩니다.

이 파이프라인으로 인해 해당 서버 개발 사이클은 인간의 로그 확인 및 즉각 대응 시간을 "0"에 수렴하게 만드는 완벽한 Self-Healing 인프라를 갖추게 되었습니다.
