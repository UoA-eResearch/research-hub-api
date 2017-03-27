package nz.ac.auckland.cer;

import nz.ac.auckland.cer.model.LifecycleCategory;
import nz.ac.auckland.cer.model.Product;
import nz.ac.auckland.cer.model.ProductCategory;
import nz.ac.auckland.cer.model.ProviderCategory;
import nz.ac.auckland.cer.repository.LifecycleCategoryRepository;
import nz.ac.auckland.cer.repository.ProductCategoryRepository;
import nz.ac.auckland.cer.repository.ProductRepository;
import nz.ac.auckland.cer.repository.ProviderCategoryRepository;
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
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProviderCategoryRepository providerCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LifecycleCategoryRepository lifecycleCategoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(ResearchHubApiApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        LifecycleCategory plan = new LifecycleCategory("Plan and Design");
        LifecycleCategory create = new LifecycleCategory("Create, Collect, Capture");
        LifecycleCategory analyse = new LifecycleCategory("Analyse and Interpret");
        LifecycleCategory publish = new LifecycleCategory("Publish and Report");
        LifecycleCategory discover = new LifecycleCategory("Discover and Re-use");

        lifecycleCategoryRepository.save(new HashSet<LifecycleCategory>() {{
            add(plan);
            add(create);
            add(analyse);
            add(publish);
            add(discover);
        }});

        ProductCategory service = new ProductCategory("Service");
        ProductCategory education = new ProductCategory("Education");

        productCategoryRepository.save(new HashSet<ProductCategory>() {{
            add(service);
            add(education);
        }});

        ProviderCategory cer = new ProviderCategory("Centre for eResearch");
        ProviderCategory library = new ProviderCategory("Library and Learning Services");

        providerCategoryRepository.save(new HashSet<ProviderCategory>() {{
            add(cer);
            add(library);
        }});

        productRepository.save(new HashSet<Product>() {{
            add(new Product("figshare", service, cer, "The University of Auckland hosts an institutional version of Figshare.", "", new HashSet<LifecycleCategory>() {{
                add(plan);
                add(create);
                add(discover);
                add(analyse);
                add(publish);
            }}));
            add(new Product("Visualisation Suite", service, cer, "The Centre for eResearch provides researchers with a range of state of the art visualisation equipment in its visualisation facility.", "", new HashSet<LifecycleCategory>() {{
                add(analyse);
                add(publish);
            }}));
            add(new Product("Research Space", service, library, "Research Space is an online archive for the University of Auckland.", "", new HashSet<LifecycleCategory>() {{
                add(plan);
                add(analyse);
                add(create);
                add(publish);
            }}));
            add(new Product("Python Workshops", education, cer, "We usually offer Python workshops for the basic and intermediate level at different times of the year.", "", new HashSet<LifecycleCategory>() {{
                add(discover);
                add(plan);
            }}));
            add(new Product("ResBaz", education, cer, "The Research Bazaar is a (free-of-charge) 3-day intensive festival and conference where researchers come together to up-skill in next generation digital research tools and scholarship.", "", new HashSet<LifecycleCategory>() {{
                add(plan);
            }}));
            add(new Product("Winter Bootcamp", education, cer, "Winter Bootcamp is a week long training and education event co-ordinated by the Center for eResearch where you can up-skill in a whole host of topics related to digital research practice.", "", new HashSet<LifecycleCategory>() {{
                add(create);
            }}));
        }});

        // fetch all categories
        for (ProductCategory productCategory : productCategoryRepository.findAll()) {
            logger.info(productCategory.toString());
        }
    }
}
