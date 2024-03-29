#!/bin/bash

set -eu
set -uo pipefail

: ${JAVA_HOME:?}

cd "$(dirname "$0")"
source conf.sh

COMMAND=""
DEFAULT_DEBUG_PORT=5050
DEBUG_COMMAND="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address="

DEBUG=""
RED='\033[0;31m'
GREEN='\033[0;32m'
NO_COLOR='\033[0m'

function error() {
  printf "${RED}%s\n${NO_COLOR}" "$1" >&2
}

function info() {
  printf "${GREEN}%s\n${NO_COLOR}" "$1"
}

TASK_PARAM=""
OUTPUT_FOLDER_PARAM=""
EXAM_PACKAGE_PARAM=""
EXAM_ITEM_FILE_PARAM=""
OUTPUT_FOLDER_PARAM=""
SOLUTION_CLASSPATH_DIR_PARAM=""
ENABLE_BYTECODE_MANIPULATION_PARAM=""

function usage() {
    echo "this will be the usage"
}

evaluate-exam() {
  COMMAND=("${JAVA_HOME}/bin/java" "${DEBUG}" -jar "${JAVA_EXAM_EVALUATOR_JAR}" \
    --task "${TASK_PARAM}" --examItemFile "${EXAM_ITEM_FILE_PARAM}" --outputFolder "${OUTPUT_FOLDER_PARAM}" \
    --solutionClassPath "${SOLUTION_CLASSPATH_DIR_PARAM}" "${ENABLE_BYTECODE_MANIPULATION_PARAM}")
}

validate-export-exam() {
  COMMAND=("${JAVA_HOME}/bin/java" "${DEBUG}" -jar "${JAVA_EXAM_VALIDATOR_JAR}" \
  --task "${TASK_PARAM}" --examPackage "${EXAM_PACKAGE_PARAM}" --outputFolder "${OUTPUT_FOLDER_PARAM}")
}

for i in "$@"; do
  case $i in
   -d|--debug)
     DEBUG="$DEBUG_COMMAND$DEFAULT_DEBUG_PORT"
     shift
     ;;
   -d=*|--debug=*)
     DEBUG="$DEBUG_COMMAND${i#*=}"
     shift
     ;;
   -task=*|--task=*)
     TASK_PARAM="${i#*=}"
     shift
     ;;
   -examPackage=*|--examPackage=*)
     EXAM_PACKAGE_PARAM="${i#*=}"
     shift
     ;;
   -examItemFile=*|--examItemFile=*)
     EXAM_ITEM_FILE_PARAM="${i#*=}"
     shift
     ;;
   -outputFolder=*|--outputFolder=*)
     OUTPUT_FOLDER_PARAM="${i#*=}"
     shift
     ;;
   -solutionClassDirPath=*|--solutionClassDirPath=*)
     SOLUTION_CLASSPATH_DIR_PARAM="${i#*=}"
     shift
     ;;
   -enableByteCodeManipulation|--enableByteCodeManipulation)
     ENABLE_BYTECODE_MANIPULATION_PARAM="--enableByteCodeManipulation"
     shift
     ;;
   -h|--help)
     usage
     exit
     shift
     ;;
   *)
     shift
     ;;
  esac
done

case "$TASK_PARAM" in
  evaluate) evaluate-exam;;
  export) validate-export-exam;;
  validate) validate-export-exam;;
  *)
    error "Argument 'task' should be one of the followings: validate|export|evaluate"
    exit 1
  ;;
esac

info "Executing: $(echo ${COMMAND[@]})"
exec "${COMMAND[@]}"