@echo off
setlocal
cls
set "BASEDIR=%~dp0"
java -jar "%BASEDIR%\runnable.jar" "%BASEDIR%\links.txt"
endlocal
