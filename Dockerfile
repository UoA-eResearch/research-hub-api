FROM            java:8
MAINTAINER      James Diprose "j.diprose@auckland.ac.nz"

ARG             http_proxy
ARG             https_proxy

# Download maven
ENV             MAVEN_VERSION 3.5.2
ENV             MAVEN_CHECKSUM 948110de4aab290033c23bf4894f7d9a

RUN             wget --no-verbose -O /apache-maven.tar.gz http://www-us.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz

# Verify checksum
RUN             echo "$MAVEN_CHECKSUM /apache-maven.tar.gz" | md5sum -c

# Install maven
RUN             tar xzf /apache-maven.tar.gz -C /opt/ && mv /opt/apache-maven-$MAVEN_VERSION /opt/maven
RUN		        echo '#!/bin/bash\n' > /usr/local/bin/mvn
RUN		        chmod 755 /usr/local/bin/mvn

# Configure proxy for maven on UoA VMs
RUN		        if [ "$http_proxy" != "" ]; then \
		            proxy=$(basename $http_proxy); host=${proxy%:*}; port=${proxy#*:}; \
		            echo "/opt/maven/bin/mvn -DproxySet=true -DproxyHost=$host -DproxyPort=$port \$*" >> /usr/local/bin/mvn; \
		        else \
		            echo "/opt/maven/bin/mvn \$*" >> /usr/local/bin/mvn; \
		        fi

#RUN             ln -s /opt/maven/bin/mvn /usr/local/bin
ENV             MAVEN_HOME /opt/maven
RUN             rm -f /apache-maven.tar.gz

# Build research hub api jar with maven
WORKDIR         /research-hub-api/

# Resolve dependencies with maven
COPY            /pom.xml /research-hub-api/pom.xml
RUN		        mvn dependency:resolve

# Copy src files and build project
COPY            /src /research-hub-api/src
# Stops maven from re-downloading dependencies
RUN             mvn verify clean --fail-never
RUN 		    mvn package
RUN             mv target/app.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.config.location=file:/application.properties","-jar","/app.jar"]
