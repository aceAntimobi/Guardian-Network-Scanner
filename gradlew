#!/usr/bin/env sh
set -e
DIR="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_JAR="$DIR/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Gradle wrapper JAR missing. Please run 'gradle wrapper' on a machine with Gradle installed.'" >&2
  exit 1
fi
JAVA_EXEC="${JAVA_HOME}/bin/java"
if [ ! -x "$JAVA_EXEC" ]; then
  JAVA_EXEC="java"
fi
exec "$JAVA_EXEC" -jar "$WRAPPER_JAR" "$@"
