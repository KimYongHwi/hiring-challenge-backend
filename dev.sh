#!/usr/bin/env bash
# 저장소 루트에서 프론트(Vite)와 백엔드(Spring Boot)를 동시에 띄웁니다.
# 사용: ./dev.sh   (실행 권한 없으면: bash dev.sh)
# 사전: cd frontend && npm install, 백엔드는 JDK 21 + ./gradlew 사용 가능해야 합니다.

set -u

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT"

# 백엔드가 뜨지 않으면 Vite 프록시가 ECONNREFUSED → 브라우저에서는 502 로 보이기 쉬워,
# Gradle이 JDK 를 잡는지 먼저 확인하고 실패 시 안내 후 종료합니다.
echo "[dev] JDK / Gradle 확인 중…"
if ! (cd "$ROOT/backend" && ./gradlew -version >/dev/null 2>&1); then
  echo "" >&2
  echo "[dev] 오류: ./gradlew -version 이 실패했습니다. (로그에 'Unable to locate a Java Runtime' 가 나온 경우가 많습니다)" >&2
  echo "[dev] → JDK 21 을 설치하고 PATH 또는 JAVA_HOME 을 맞춘 뒤 다시 ./dev.sh 를 실행하세요." >&2
  echo "[dev]   예: brew install openjdk@21  후 PATH 설정, 또는 SDKMAN, IDE 번들 JDK" >&2
  echo "[dev] → Docker 로만 백엔드를 띄울 경우: docker compose up api (프론트는 별도 npm run dev, 프록시 타겟은 8080)" >&2
  echo "" >&2
  exit 1
fi

if [[ ! -d "$ROOT/frontend/node_modules" ]]; then
  echo "[dev] 안내: frontend/node_modules 가 없습니다. 먼저 실행해 주세요: cd frontend && npm install" >&2
  exit 1
fi

echo "[dev] frontend → http://localhost:5173 (Vite, /api 는 8080 프록시)"
echo "[dev] backend  → http://localhost:8080"
echo "[dev] 종료: Ctrl+C"
echo ""

cleanup() {
  # 남아 있는 백그라운드 잡(자식)을 정리합니다.
  # macOS 기본 bash(3.2)에서도 동작하도록 wait -n 은 쓰지 않습니다.
  local pids
  pids="$(jobs -p)"
  if [[ -n "${pids}" ]]; then
    kill ${pids} 2>/dev/null || true
  fi
}

trap cleanup INT TERM EXIT

(
  cd "$ROOT/frontend"
  npm run dev
) &

(
  cd "$ROOT/backend"
  ./gradlew bootRun --no-daemon
) &

wait
cleanup
