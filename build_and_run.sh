#!/bin/bash
set -e

mvn package -Dquarkus.package.type=uber-jar
# Create a new docker image if necessary.
# Restarts the container with the new image if necessary
# The server stays running.
# To terminate the server run docker-compose down in the
# code-with-quarkus direcgtory
docker-compose up -d
# clean up images
docker image prune -f