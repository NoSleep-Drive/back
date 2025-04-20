#!/bin/bash

APP_DIR="/home/noSleepDrive/app/backend"
JAR_NAME=$(ls $APP_DIR/*.jar | head -n 1)
PID_FILE="$APP_DIR/app.pid"
LOG_FILE="$APP_DIR/app.log"
ENV_FILE="$APP_DIR/.env"

echo "[start script]"

# 0. Load env variables
if [ -f "$ENV_FILE" ]; then
  echo "Loading environment variables from .env"
  export $(grep -v '^#' "$ENV_FILE" | xargs)
else
  echo ".env file not found"
fi

# 1. terminate previous process
if [ -f "$PID_FILE" ]; then
    PID=$(cat $PID_FILE)
    echo "terminate previous process (PID: $PID)"
    kill -9 $PID
    rm -f $PID_FILE
else
    echo "process is not running"
fi

# 2. start new JAR
echo "execute a new JAR: $JAR_NAME"
nohup java -jar $JAR_NAME > $LOG_FILE 2>&1 &

# 3. save new PID
echo $! > $PID_FILE
echo "complete executing (PID: $(cat $PID_FILE))"
