#!/bin/bash

INPUT="/home/mike/git/tv-submit/mock-sentiment.data"
#TVHOST=https://tv-submit-labs-dev.apps.r2r.vic.apac.rht-labs.com
TVHOST=http://localhost:8081

while IFS= read -r line
do
    echo $line
    curl -i -H "Content-Type: application/json" -X POST -d "$line" ${TVHOST}/submission
done < "$INPUT"
