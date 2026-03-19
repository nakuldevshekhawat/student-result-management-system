#!/bin/bash
# ============================================================
#  Student Result Management System - Build & Run Script
# ============================================================
#
#  Prerequisites:
#    - Java 11+  (java & javac on PATH)
#    - MySQL 8+  running on localhost:3306
#    - mysql-connector-j-8.x.x.jar placed in ./lib/
#
#  Usage:
#    chmod +x build.sh
#    ./build.sh          # compile only
#    ./build.sh run      # compile + run

set -e

SRC_DIR="src/main/java"
OUT_DIR="out"
JAR_LIB="lib/mysql-connector-j.jar"
MAIN_CLASS="com.srms.ui.MainMenu"

echo "=== Compiling ==="
mkdir -p "$OUT_DIR"

# Find all .java files
find "$SRC_DIR" -name "*.java" > sources.txt

javac -cp "$JAR_LIB" -d "$OUT_DIR" @sources.txt
rm sources.txt

echo "=== Compilation successful ==="

if [ "$1" == "run" ]; then
    echo "=== Starting Student Result Management System ==="
    java -cp "$OUT_DIR:$JAR_LIB" "$MAIN_CLASS"
fi
