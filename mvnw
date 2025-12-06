#!/usr/bin/env bash
# Minimal Maven Wrapper for Unix shells - forwards to run-tests.ps1 via pwsh if available
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if command -v pwsh >/dev/null 2>&1; then
  pwsh -NoProfile -ExecutionPolicy Bypass -File "${SCRIPT_DIR}/run-tests.ps1" "$@"
elif command -v powershell >/dev/null 2>&1; then
  powershell -NoProfile -ExecutionPolicy Bypass -File "${SCRIPT_DIR}/run-tests.ps1" "$@"
else
  echo "Could not find 'pwsh' or 'powershell' to run the wrapper script."
  exit 1
fi
