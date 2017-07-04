package nz.ac.auckland.cer;

import nz.ac.auckland.cer.model.Content;
import nz.ac.auckland.cer.model.ContentRole;
import nz.ac.auckland.cer.model.Person;
import nz.ac.auckland.cer.model.categories.*;
import nz.ac.auckland.cer.repository.*;
import org.hibernate.collection.internal.PersistentBag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;

@SpringBootApplication
public class ResearchHubApiApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ResearchHubApiApplication.class);

    @Autowired
    private ContentTypeRepository contentTypeRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ResearchPhaseRepository researchPhaseRepository;

    @Autowired
    private ExternalUrlRepository externalUrlRepository;

    @Autowired
    private ContentRoleRepository contentRoleRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(ResearchHubApiApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        // Life cycle stages
        ResearchPhase plan = new ResearchPhase("Plan and Design");
        ResearchPhase create = new ResearchPhase("Create, Collect, Capture");
        ResearchPhase analyse = new ResearchPhase("Analyse and Interpret");
        ResearchPhase publish = new ResearchPhase("Publish and Report");
        ResearchPhase discover = new ResearchPhase("Discover and Re-use");

        researchPhaseRepository.save(new HashSet<ResearchPhase>() {{
            add(plan);
            add(create);
            add(analyse);
            add(publish);
            add(discover);
        }});

        // Service types
        ContentType service = new ContentType("Software");
        ContentType education = new ContentType("Training");

        contentTypeRepository.save(new HashSet<ContentType>() {{
            add(service);
            add(education);
        }});


        // Create dummy products
        Content figshare = new Content(service, "figshare", "The University of Auckland hosts an institutional version of Figshare.", "cat.jpg", "", "", "");
//        figshare.setCreated(LocalDateTime.now());

        figshare.setResearchPhases(new HashSet<ResearchPhase>(){{
            add(publish);
            add(discover);
        }});

        Content winterBootcamp = new Content(education, "Winter Bootcamp", "Winter Bootcamp is a week long training and education event co-ordinated by the Center for eResearch where you can up-skill in a whole host of topics related to digital research practice.", "cat.jpg", "", "", "");
//        winterBootcamp.setCreated(LocalDateTime.now());
        winterBootcamp.setResearchPhases(new HashSet<ResearchPhase>(){{
            add(analyse);
        }});

        contentRepository.save(new HashSet<Content>() {{
            add(figshare);
            add(winterBootcamp);
        }});

        externalUrlRepository.save(new HashSet<ExternalUrl>(){{
            add(new ExternalUrl("Google", "google.com", figshare));
        }});

        Role slaver = new Role("Slaver");
        Role slave = new Role("Slave");

        roleRepository.save(new HashSet<Role>() {{
            add(slaver);
            add(slave);
        }});

        Person jack = new Person("Jack","Bauer","j@bauer.com","imdb.com");
        Person john = new Person("John","Doe","j@doe.com","google.com");

        personRepository.save(new HashSet<Person>() {{
            add(jack);
            add(john);
        }});

        ContentRole contentRoleA = new ContentRole(figshare, jack, slaver);
        ContentRole contentRoleB = new ContentRole(figshare, john, slave);
        ContentRole contentRoleC = new ContentRole(winterBootcamp, john, slaver);
        ContentRole contentRoleD = new ContentRole(winterBootcamp, jack, slave);

        contentRoleRepository.save(new HashSet<ContentRole>() {{
            add(contentRoleA);
            add(contentRoleB);
            add(contentRoleC);
            add(contentRoleD);
        }});
    }
}
