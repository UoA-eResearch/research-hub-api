FROM            maven:3.5.4-jdk-8 AS prepare
MAINTAINER      Sam Kavanagh "s.kavanagh@auckland.ac.nz"

ARG             http_proxy
ARG             https_proxy

ENV             MAVEN_HOME /opt/maven

# Build research hub api jar with maven
WORKDIR         /research-hub-api/

# Configure proxy for maven on UoA VMs


FROM		prepare AS local
# Create a user in the image that has the same UID as the host user and run the Docker image as the user, so that generated classfiles can be shared between host and image.
# Default user id to 1000 if not set in hub.env.
ARG		current_uid=1000
RUN		useradd -m --uid $current_uid rhapi-user
VOLUME		["/research-hub-api/src","research-hub-api/target"]
ENTRYPOINT	["/docker-entrypoint.sh","--local"]


FROM		prepare AS build
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


# Copy src files and build project
COPY            /src /research-hub-api/src
COPY            application.properties /
COPY		docker-entrypoint.sh /
RUN             mvn -o package
RUN             mv target/app.jar /app.jar

ENTRYPOINT      ["/docker-entrypoint.sh"]