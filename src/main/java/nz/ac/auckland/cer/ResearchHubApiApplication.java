package nz.ac.auckland.cer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;

@SpringBootApplication
public class ResearchHubApiApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ResearchHubApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ResearchHubApiApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {

    }
}
