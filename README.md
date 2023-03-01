# Description

Repository for Kuehne-nagel test project

## Local installation
1. Need to be installed:
   - Java 17+ (optional if we want to develop locally)
   - Docker Engine 19.03.0+ (Or latest, because we are using [Docker compose 3.8](https://docs.docker.com/compose/compose-file/compose-versioning/)) 
   - Maven 3.8+
2. Run in terminal or through the IDEA **mvn clean install**
3. Run in terminal **docker-compose up -d**, or run [docker-compose](docker-compose.yml) via IDEA
4. By default, server is running on [localhost:8080](http://localhost:8080)
