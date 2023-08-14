#!/bin/bash

# 기존 jar 실행 PID 탐색 및 실행 중지
echo "> sudo lsof -i:8080 | grep java | grep -v grep | awk '{print $2}' "
sudo kill -15 ` sudo lsof -i:8080 | grep java | grep -v grep | awk '{print $2}' `

# 기존 jar 삭제
sudo rm -rf practice-0.0.1-SNAPSHOT.jar