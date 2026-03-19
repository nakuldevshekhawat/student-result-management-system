@echo off
REM ============================================================
REM  Student Result Management System - Build & Run (Windows)
REM ============================================================
REM  Prerequisites:
REM    - Java 11+  (java & javac on PATH)
REM    - MySQL 8+  running on localhost:3306
REM    - mysql-connector-j-8.x.x.jar in .\lib\
REM
REM  Usage:
REM    build.bat          -> compile only
REM    build.bat run      -> compile + run

SET SRC_DIR=src\main\java
SET OUT_DIR=out
SET JAR_LIB=lib\mysql-connector-j.jar
SET MAIN_CLASS=com.srms.ui.MainMenu

echo === Compiling ===
if not exist %OUT_DIR% mkdir %OUT_DIR%

dir /s /b %SRC_DIR%\*.java > sources.txt
javac -cp %JAR_LIB% -d %OUT_DIR% @sources.txt
del sources.txt

echo === Compilation successful ===

IF "%1"=="run" (
    echo === Starting Student Result Management System ===
    java -cp "%OUT_DIR%;%JAR_LIB%" %MAIN_CLASS%
)
