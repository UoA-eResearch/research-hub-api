FROM            java:8
MAINTAINER      James Diprose "j.diprose@auckland.ac.nz"

ARG             http_proxy
ARG             https_proxy

# Dependencies for auto_cer
RUN             apt-get update && apt-get install -y python3 python3-pip && pip3 install requests pymysql prettytable urllib3 selenium selenium-requests

# Download maven
ENV             MAVEN_VERSION 3.5.0
ENV             MAVEN_CHECKSUM 35c39251d2af99b6624d40d801f6ff02

RUN             wget --no-verbose -O /apache-maven.tar.gz http://www-us.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz

# Verify checksum
RUN             echo "$MAVEN_CHECKSUM /apache-maven.tar.gz" | md5sum -c

# Install maven
RUN             tar xzf /apache-maven.tar.gz -C /opt/ && mv /opt/apache-maven-$MAVEN_VERSION /opt/maven
RUN             ln -s /opt/maven/bin/mvn /usr/local/bin
ENV             MAVEN_HOME /opt/maven
RUN             rm -f /apache-maven.tar.gz

# Configure proxy for maven on UoA VMs
RUN             if [ "$http_proxy" != "" ] && [ "$https_proxy" != "" ] ; then proxy=$(basename $http_proxy); host=${proxy%:*}; port=${proxy#*:}; mvn -DproxySet=true -DproxyHost=$host -DproxyPort=$port package ; else echo 'Not setting proxy: http_proxy and https_proxy are empty' ; fi

# Build research hub api jar with maven
WORKDIR         /research-hub-api/

# Resolve dependencies with maven
COPY            /pom.xml /research-hub-api/pom.xml
RUN             mvn dependency:resolve

# Copy src files and build project
COPY            /src /research-hub-api/src
# Stops maven from re-downloading dependencies
RUN             mvn verify clean --fail-never
RUN 		    mvn package
RUN             mv target/app.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.config.location=file:/application.properties","-jar","/app.jar"]