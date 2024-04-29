#!/bin/bash

PROJECT_DIR="."

cd "$PROJECT_DIR"

mvn clean compile

for i in {1..5}
do
  mvn exec:java -Dexec.mainClass="ar.edu.itba.ss.Main" -Dexec.args="-O=output/v10/msd_$i.csv"
done

