#!/bin/bash

# run the application with maven because a known bug: https://github.com/spring-projects/spring-boot/issues/6709
RUN="mvn spring-boot:run"
NAME=goeuro-routes-check-service
LOG="spring-neo4j.log"

ACTION=$1
FILE=$2

$RUN -Drun.arguments=$ACTION,$FILE
