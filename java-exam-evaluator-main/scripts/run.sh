#!/bin/bash

cd "$(dirname "$0")"

# TODO
# D:\Tools\jdk-17.0.4.1\bin\java.exe -classpath \..\java-exam-test-solution\target\java-exam-test-solution-20230101-SNAPSHOT.jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5050 -jar java-exam-evaluator-20230101-SNAPSHOT.jar --task evaluateExam --examPackage hu.sed.evaluator.exam.sample --outputFolder c:\Users\rkalmar\Desktop\szte\diplomamunka\outputs --examItemFile c:\Users\rkalmar\Desktop\szte\diplomamunka\outputs\examfile