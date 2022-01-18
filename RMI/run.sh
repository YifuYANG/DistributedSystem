#!/bin/bash

echo "Cleaning docker images and containers:"
docker stop $(docker ps -aq)
docker rm $(docker ps -aq)
docker rmi $(docker images -q)

echo "Packaging maven project:"
mvn clean install package

echo "Running docker compose:"
docker-compose up