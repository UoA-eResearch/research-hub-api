# Research Hub API

The Research Hub REST API.
 
## Requirements

* Spring Boot
* Maven
* MySQL server 5.7
* IntelliJ IDE (until I have time to write documentation on how to do this from the command line)

## Development
To generate the QueryDSL classes: right click pom.xml > Maven > Generate Sources and Update Folders.

Build project: from Menu, click Build > Build Project.

Make sure the MySQL user is `root` and password `123` (you can change this from application.properties)

Create the database by executing `src/main/resources/db.sql` from the MySQL command line.

To run: right click on ResearchHubApiApplication > Run (or debug)

