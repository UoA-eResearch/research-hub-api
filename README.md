# Research Hub API
The REST API for the [Research Hub](https://research-hub.auckland.ac.nz/), developed with [Spring Boot](https://projects.spring.io/spring-boot/).

Before running the Research Hub API, follow the instructions on the [Research Hub Deploy project](https://github.com/UoA-eResearch/research-hub-deploy#research-hub-deploy) to run the research-hub-db Docker container.

## Requirements
* Maven `sudo apt install maven`
* IntelliJ IDE (optional)

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
