# Research Hub API

The Research Hub REST API.

To deploy the Research Hub follow the instructions on the [Research Hub Deploy project](https://github.com/UoA-eResearch/research-hub-deploy#research-hub-deployment-project).
 
## Requirements
* Spring Boot
* Maven `sudo apt install maven`
* MySQL server 5.7
* IntelliJ IDE (until I have time to write documentation on how to do this from the command line)
* [Seed the MySQL database](https://github.com/UoA-eResearch/research-hub-deploy#seeding-a-database-manually).

## Development
To generate the QueryDSL classes: right click pom.xml > Maven > Generate Sources and Update Folders.

Build project: from Menu, click Build > Build Project.

Make sure the MySQL user is `root` and password `123` (you can change this from application.properties)

Create the database by executing `src/main/resources/db.sql` from the MySQL command line.

To run: right click on ResearchHubApiApplication > Run (or debug)


File > Project structure > Modules > 

Tag target/generated-sources as a Sources Folder and target/generated-test-sources as a Test Source Folder.
