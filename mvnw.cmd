@echo off
REM Maven Wrapper for Windows - forwards args to run-tests.ps1 which downloads temporary Maven if needed
SETLOCAL
SET SCRIPT_DIR=%~dp0



EXIT /B %ERRORLEVEL%powershell -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%run-tests.ps1" %*n