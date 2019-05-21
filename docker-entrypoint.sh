#!/bin/bash

if [ "$1" == "--local" ]; then
    # Change the permission for 
    chown -R rhapi-user /research-hub-api/target/
    # If the local flag is passed, we run a version of the rh api project set up for development.
    # Run as user with uid matching the host user, so generated directories and classfiles
    # can be accessed/changed by host.
    su - rhapi-user
    mvn dependency:go-offline
    # Run a version of spring boot which watches the /target directory, and reloads when
    # generated classfiles change.
    mvn spring-boot:run -Drun.jvmArguments=-Dspring.config.location=/application.properties
    exit
fi
