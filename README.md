# Overview

System is using spring state machine to manage work flow

# Build the system

System build is based on gradle build, so you can use regular gradle commands to build the system

```
gradlew clean build
```

Also you can run unit test using regular maven

```
gradlew test
```

# Startup local environment

The system is based on the following:
1) Mysql database: database is optional, and you can start the system without database, and it will use RAM database
3) spring boot: the system is build on spring boot

### Note:

you can use docker file on "local" path to start mysql

```
cd local
docker-compose up
```

After starting up MySql (optional) you can run regular spring boot commands to start up the system

```
gradlew bootRun
```

# Api Documentation

Swagger is the main api documentation, To access Swagger:

http://localhost:8081/swagger-ui/index.html#/
