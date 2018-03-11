# Research Hub API

The Research Hub REST API project. First follow the instructions on the [Research Hub Deploy project](https://github.com/UoA-eResearch/research-hub-deploy) to run the research-hub-db Docker container.

## Requirements
* Spring Boot
* Maven `sudo apt install maven`
* IntelliJ IDE (until I have time to write documentation on how to do this from the command line)

## Using the command line
Make sure the database is running first (see link at top of page).

To build the project:
```bash
mvn clean package -Dspring.profiles.active="local"
```
To run the project:
```bash
mvn spring-boot:run -Dspring.profiles.active="local"
```

## Using IntelliJ
Build project: from Menu, click Build > Build Project.

To run: right click on ResearchHubApiApplication > Run (or debug)
