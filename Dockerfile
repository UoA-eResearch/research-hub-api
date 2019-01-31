FROM            maven:3.5.4-jdk-8 AS prepare
MAINTAINER      Sam Kavanagh "s.kavanagh@auckland.ac.nz"

ARG             http_proxy
ARG             https_proxy

ENV             MAVEN_HOME /opt/maven

# Build research hub api jar with maven
WORKDIR         /research-hub-api/

# Resolve dependencies with maven, stops maven from re-downloading dependencies
COPY            /pom.xml /research-hub-api/pom.xml

# Configure proxy for maven on UoA VMs
RUN		if [ -z $http_proxy ]; then \
			mvn dependency:go-offline; \
			mvn verify clean --fail-never; \
		else \
			proxy=$(basename $http_proxy); host=${proxy%:*}; port=${proxy#*:}; \
			export MAVEN_OPTS="-DproxySet=true -DproxyHost=$host -DproxyPort=$port"; \
			echo $MAVEN_OPTS; \
			mvn dependency:go-offline; \
			mvn verify clean --fail-never; \
		fi;

FROM		prepare AS dev
VOLUME		["/research-hub-api/src","/application.properties","research-hub-api/target"]

ENTRYPOINT	["mvn","spring-boot:run","-Drun.jvmArguments=-Dspring.config.location=/application.properties"]


FROM		prepare AS build

# Copy src files and build project
COPY            /src /research-hub-api/src
COPY            application.properties /
RUN             mvn -o package
RUN             mv target/app.jar /app.jar


ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.config.location=file:/application.properties","-jar","/app.jar"]
