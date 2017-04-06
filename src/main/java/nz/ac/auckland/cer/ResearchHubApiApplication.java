package nz.ac.auckland.cer;

import nz.ac.auckland.cer.model.Product;
import nz.ac.auckland.cer.model.categories.*;
import nz.ac.auckland.cer.model.content.*;
import nz.ac.auckland.cer.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.HashSet;

@SpringBootApplication
public class ResearchHubApiApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ResearchHubApiApplication.class);

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LifeCycleRepository lifeCycleRepository;

    @Autowired
    private ProgrammeRepository programmeRepository;

    @Autowired
    private EligibilityRepository eligibilityRepository;

    @Autowired
    private StudyLevelRepository studyLevelRepository;

    @Autowired
    private CostRepository costRepository;

    @Autowired
    private RequirementRepository requirementRepository;

    @Autowired
    private ConsiderationRepository considerationRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private LimitationRepository limitationRepository;

    @Autowired
    private SupportRepository supportRepository;

    @Autowired
    private ReferenceRepository referenceRepository;

    @Autowired
    private ContactRepository contactRepository;

    public static void main(String[] args) {
        SpringApplication.run(ResearchHubApiApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        // Life cycle stages
        LifeCycle plan = new LifeCycle("Plan and Design");
        LifeCycle create = new LifeCycle("Create, Collect, Capture");
        LifeCycle analyse = new LifeCycle("Analyse and Interpret");
        LifeCycle publish = new LifeCycle("Publish and Report");
        LifeCycle discover = new LifeCycle("Discover and Re-use");

        lifeCycleRepository.save(new HashSet<LifeCycle>() {{
            add(plan);
            add(create);
            add(analyse);
            add(publish);
            add(discover);
        }});

        // Service types
        ProductType service = new ProductType("Service");
        ProductType education = new ProductType("Education");

        productTypeRepository.save(new HashSet<ProductType>() {{
            add(service);
            add(education);
        }});

        // Providers
        Provider cer = new Provider("Centre for eResearch");
        Provider library = new Provider("Library and Learning Services");

        providerRepository.save(new HashSet<Provider>() {{
            add(cer);
            add(library);
        }});

        // Programme
        Programme progAll = new Programme("All programmes");
        Programme progScience = new Programme("Science");

        programmeRepository.save(new HashSet<Programme>() {{
            add(progAll);
            add(progScience);
        }});

        // Study level
        StudyLevel studyAll = new StudyLevel("All study levels");
        StudyLevel studyBachelors = new StudyLevel("Bachelor degrees");

        studyLevelRepository.save(new HashSet<StudyLevel>() {{
            add(studyAll);
            add(studyBachelors);
        }});

        // Eligibility
        Eligibility eligibleUndergrad = new Eligibility("Undergrad");
        Eligibility eligiblePostgrad = new Eligibility("Postgrad");
        Eligibility eligibleProfStaff = new Eligibility("Prof staff");
        Eligibility eligibleAcadStaff = new Eligibility("Acad");

        eligibilityRepository.save(new HashSet<Eligibility>() {{
            add(eligibleUndergrad);
            add(eligiblePostgrad);
            add(eligibleProfStaff);
            add(eligibleAcadStaff);
        }});

        // Cost
        Cost costFree = new Cost("Free");
        Cost costPaid = new Cost("Paid");

        costRepository.save(new HashSet<Cost>() {{
            add(costFree);
            add(costPaid);
        }});

        // Create dummy products
        Product figshare = new Product("figshare", "The University of Auckland hosts an institutional version of Figshare.", "cat.jpg", service, cer);
        figshare.setEligibleGroups(new HashSet<Eligibility>() {{
            add(eligibleAcadStaff);
        }});

        figshare.setCosts(new HashSet<Cost>() {{
            add(costFree);
        }});
        figshare.setLifeCycleStages(new HashSet<LifeCycle>(){{
            add(publish);
            add(discover);
        }});

        Product winterBootcamp = new Product("Winter Bootcamp", "Winter Bootcamp is a week long training and education event co-ordinated by the Center for eResearch where you can up-skill in a whole host of topics related to digital research practice.", "cat.jpg", education, cer);
        winterBootcamp.setStudyLevels(new HashSet<StudyLevel>(){{
            add(studyAll);
        }});
        winterBootcamp.setCosts(new HashSet<Cost>(){{
            add(costFree);
        }});

        figshare.setLifeCycleStages(new HashSet<LifeCycle>(){{
            add(analyse);
        }});

        productRepository.save(new HashSet<Product>() {{
            add(figshare);
            add(winterBootcamp);
        }});

        requirementRepository.save(new HashSet<Requirement>(){{
            add(new Requirement("University of Auckland UPI", figshare));
        }});

        featureRepository.save(new HashSet<Feature>(){{
            add(new Feature("Content uploaded and published via Figshare is assigned a persistent digital identifier (DOI) through DataCite and indexed by Google Scholar", figshare));
            add(new Feature("Research groups can work together in projects and shared collections to collaboratively curate and edit data items", figshare));
            add(new Feature("For sensitive data, Figshare allows the publishing of only metadata associated with your research data, making people aware that this data exists and could maybe be used under certain conditions by contacting the owner", figshare));
            add(new Feature("Data published in Figshare can be private or public, or embargoed until a specified date", figshare));
        }});

        limitationRepository.save(new HashSet<Limitation>(){{
            add(new Limitation("Not suitable for active data that is still changing", figshare));
            add(new Limitation("Not suitable for data that is not owned by you", figshare));
            add(new Limitation("Not suitable for data with special privacy or security concerns, although metadata only records can be published to broadcast the existence of such datasets", figshare));
        }});

        considerationRepository.save(new HashSet<Consideration>(){{
            add(new Consideration("Figshare is under active development and additional features and services will be developed over time", figshare));
            add(new Consideration("Once published publically, data can not be ‘unpublished’", figshare));
            add(new Consideration("Consider use of domain specific metadata schemas to describe your published data", figshare));
            add(new Consideration("If you have a large dataset or number of files, consider using the Figshare API", figshare));
            add(new Consideration("Detailed specifications: The Figshare service is hosted in Ireland, with all data published ultimately being stored on Auckland University Storage Systems", figshare));
            add(new Consideration("A REST API is available", figshare));
        }});

        supportRepository.save(new HashSet<Support>(){{
            add(new Support("General support is provided through the Library and Learning Services Research Support", "http://www.library.auckland.ac.nz/services/research-support", figshare));
            add(new Support("For technical support or for uploading datasets larger than 100GB contact CeR ", "mailto:eresearch-support@auckland.ac.nz", figshare));
        }});

        referenceRepository.save(new HashSet<Reference>(){{
            add(new Reference("Figshare users guide", "https://figshare.zendesk.com/hc/en-us/articles/201953903-How-do-I-sign-up-", figshare));
            add(new Reference("Figshare FAQs", "https://figshare.zendesk.com/hc/en-us", figshare));
        }});

        contactRepository.save(new HashSet<Contact>(){{
            add(new Contact("Marcus Binsteiner", "m.binsteiner@auckland.ac.nz", figshare));
            add(new Contact("Marcus Binsteiner", "m.binsteiner@auckland.ac.nz", winterBootcamp));
        }});

//        QProvider provider;

//        QProduct product = QProduct.product;
//        JPQLQuery query = new HibernateQuery (session);
//        Product bob = query.from(product)
//                .where(product.summary.like("%ootcamp%"))
//                .uniqueResult(product);


    }
}
