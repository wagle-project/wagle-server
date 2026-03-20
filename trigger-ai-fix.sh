#!/bin/bash
# trigger-ai-fix.sh
# GitHub Action 실패 Webhook 수신 시 n8n에서 이 스크립트를 호출하여 로컬 AI(Claude Code, Antigravity 등)를 구동합니다.
# 사용법: ./trigger-ai-fix.sh <github_run_id> <failure_count> <model_tier> <log_file_or_text>

PROJECT_DIR="/Users/seoyeon/Projects/YUProjects/wagle-server"
RUN_ID=$1
FAIL_COUNT=$2
MODEL_TIER=$3
LOG_TEXT=$4

echo "====================================="
echo "[AI Fix Triggered] Run ID: $RUN_ID | Retry: $FAIL_COUNT | Model: $MODEL_TIER"
echo "====================================="

cd "$PROJECT_DIR" || exit 1

# 노드 및 모델 티어별 명령어 설정
# 사용자 요청 방식에 맞춘 Antigravity 전용 백그라운드 명령어
AI_CLI_COMMAND="antigravity chat"

# 15회 이상 실패 시 안전장치 (휴먼 개입) - n8n에서 사전에 걸러지지만 이중 방어
if [ "$FAIL_COUNT" -ge 15 ]; then
  echo "🚨 [치명적 오류] 15회 연속 자동 복구 실패. 스크립트를 중단합니다."
  exit 1
fi

# 프롬프트 구성 (자동 생성, 코드 수정, PR 생성, 노션 기록까지 명령)
PROMPT="당신은 wagle-server 프로젝트의 자가 치유(Self-Healing) CI/CD 에이전트입니다.
현재 CI 시스템에서 아래와 같은 실패 로그가 수신되었습니다. (시도 횟수: ${FAIL_COUNT}회)

<error_log>
${LOG_TEXT}
</error_log>

다음 5단계를 철저히 수행해 주세요:
1. 현재 브랜치에서 분기하여 'auto-fix/fix-pr-${RUN_ID}-${FAIL_COUNT}' 이름의 원격 및 로컬 브랜치를 생성하세요.
2. 로그를 기반으로 프로젝트 내 에러 원인 파일을 찾아 코드를 올바르게 수정하세요.
3. 수정 후 commit 및 push를 진행하세요.
4. GitHub CLI 등을 사용하여 원본 브랜치로 Pull Request를 생성하세요. (PR 제목: 'Auto-fix for CI Run ${RUN_ID} (Attempt ${FAIL_COUNT})')
5. Notion MCP를 사용해 발생한 문제의 원인, 수정한 코드 블록, 트러블슈팅 내역을 새로운 노션 하위 페이지(부모 페이지 ID: 328b0540-790a-80bc-99a4-e7f047e5373a)에 생성 및 기록하세요.

만약 코드로 수정할 수 없는 인프라 오류나 권한 이슈라면 코드 수정을 진행하지 말고 'MANUAL_INTERVENTION_REQUIRED' 라는 텍스트를 출력한 후 작업을 종료하세요."

# 임시 파일에 프롬프트 저장 (특수문자 및 따옴표 충돌 방지)
PROMPT_FILE="$PROJECT_DIR/.tmp_ai_prompt_${RUN_ID}.txt"
echo "$PROMPT" > "$PROMPT_FILE"

echo ">> AI 에이전트를 실행합니다..."
# 실제 AI 로컬 명령어 구동 (프롬프트 내용을 문자열 인자로 전달)
$AI_CLI_COMMAND "$(cat "$PROMPT_FILE")"

rm "$PROMPT_FILE"
echo ">> AI 에이전트 실행이 완료되었습니다."
