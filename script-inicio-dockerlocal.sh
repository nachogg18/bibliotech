#!/bin/bash

mvn clean package spring-boot:repackage -U

docker-compose down --remove-orphans

docker-compose build --no-cache

docker-compose -f docker-compose-dockerlocal.yml up --force-recreate

