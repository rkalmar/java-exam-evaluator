#!/bin/bash

cd "$(dirname "$0")"

# TODO

# debug  -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5050

# evaluate
# D:\Tools\jdk-17.0.4.1\bin\java.exe -cp "evaluate_test\*" hu.sed.evaluator.MainModule
#   --task evaluate
#   --examItemFile c:\Users\rkalmar\Desktop\szte\diplomamunka\outputs\examfile
#   --outputFolder c:\Users\rkalmar\Desktop\szte\diplomamunka\outputs

# validate
#D:\Tools\jdk-17.0.4.1\bin\java.exe -cp "evaluate_test\validator\*" hu.sed.evaluator.MainModule
#  --task validate
#  --examPackage hu.sed.evaluator.exam.sample
#  --outputFolder c:\Users\rkalmar\Desktop\szte\diplomamunka\outputs

#  export
#D:\Tools\jdk-17.0.4.1\bin\java.exe -cp "evaluate_test\validator\*" hu.sed.evaluator.MainModule
#  --task export
#  --examPackage hu.sed.evaluator.exam.sample
#  --outputFolder c:\Users\rkalmar\Desktop\szte\diplomamunka\outputs