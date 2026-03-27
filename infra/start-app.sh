#!/bin/sh

echo "🚀 Spring Boot 서버를 시작합니다..."

# 파이프(|) 뒤쪽 명령어의 성공 여부가 아니라 앞쪽(java) 명령어의 실패 코드를 캡처하도록 설정
set -o pipefail

# 1. 서버 실행 및 화면(docker logs) 출력과 동시에 app.log 파일로도 백업 (tee 명령어 활용)
java -Duser.timezone=Asia/Seoul -jar app.jar 2>&1 | tee app.log

# 2. 애플리케이션 종료 코드 캡처
EXIT_CODE=$?

# 3. 비정상 종료(에러) 발생 시 Webhook 전송 로직
if [ "$EXIT_CODE" -ne 0 ]; then
  echo "🚨 [치명적 에러] Spring Boot 앱이 비정상 종료되었습니다!"
  echo ">> n8n 자가 치유 에이전트(AI)에게 SOS 구조 신호를 보냅니다..."
  
  # 에러 로그 마지막 200줄만 긁어서, JSON 전송 시 터지지 않게 jq로 인코딩 처리
  ERROR_LOG=$(tail -n 200 app.log | jq -Rs .)
  
  # n8n Webhook 호출 (ngrok 주소)
  curl -X POST \
       -H "Content-Type: application/json" \
       -d "{\"event\": \"server_startup_crash\", \"error_log\": ${ERROR_LOG}}" \
       https://charity-tamest-adiabatically.ngrok-free.dev/webhook/server-error-event
       
  echo ">> AI 에이전트로 에러 발송 완료!"
fi

# 4. 마지막에 잡은 실제 에러 코드를 그대로 뱉어서 Docker 데몬도 실패를 인지하게 만듦
exit $EXIT_CODE
