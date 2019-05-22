#!/bin/bash

if [ "$1" == "--local" ]; then
    # If the local flag is passed, we run a version of the rh api project set up for development.
    # Run as user with uid matching the host user, so generated directories and classfiles
    # can be accessed/changed by host.
    # Change the permission for the target folder.
    chown -R rhapi-user /research-hub-api/target/
    su rhapi-user -c 'mvn dependency:go-offline'
    # Run a version of spring boot which watches the /target directory, and reloads when
    # generated classfiles change.
    su rhapi-user -c 'mvn spring-boot:run -Drun.jvmArguments=-Dspring.config.location=/application.properties'
    exit
fi

java -Djava.security.egd=file:/dev/./urandom -Dspring.config.location=file:/application.properties -jar /app.jar
