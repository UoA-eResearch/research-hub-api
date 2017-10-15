gROM            java:8
MAINTAINER      James Diprose "j.diprose@auckland.ac.nz"

ARG             http_proxy
ARG             https_proxy

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


# Copies all files and maintains directory structure
COPY            / /research-hub-api/

# Build research hub api jar with Maven
WORKDIR         /research-hub-api/
RUN             proxy=$(basename $http_proxy); host=${proxy%:*}; port=${proxy#*:}; mvn -DproxySet=true -DproxyHost=$host -DproxyPort=$port package
RUN 		mvn package
RUN             mv target/app.jar /app.jar

# Dependencies for auto_cer
RUN             apt-get update && apt-get install -y python3 python3-pip && pip3 install requests pymysql prettytable urllib3 selenium selenium-requests

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.config.location=file:/application.properties","-jar","/app.jar"]
