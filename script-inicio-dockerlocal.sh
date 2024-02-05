#!/bin/bash

mvn clean package -U

docker-compose down --remove-orphans

docker-compose build --no-cache

docker-compose -f docker-compose-dockerlocal.yml up --force-recreate

