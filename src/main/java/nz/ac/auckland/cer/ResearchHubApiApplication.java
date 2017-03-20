package nz.ac.auckland.cer;

import nz.ac.auckland.cer.model.Product;
import nz.ac.auckland.cer.model.ProductCategory;
import nz.ac.auckland.cer.model.ProviderCategory;
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
import java.util.Set;

@SpringBootApplication
public class ResearchHubApiApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ResearchHubApiApplication.class);

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProviderCategoryRepository providerCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(ResearchHubApiApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
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
            add(new Product("figshare", service, cer, "The University of Auckland hosts an institutional version of Figshare.", ""));
            add(new Product("Visualisation Suite", service, cer, "The Centre for eResearch provides researchers with a range of state of the art visualisation equipment in its visualisation facility.", ""));
            add(new Product("Research Space", service, library, "Research Space is an online archive for the University of Auckland.", ""));
            add(new Product("Python Workshops", education, cer, "We usually offer Python workshops for the basic and intermediate level at different times of the year.", ""));
            add(new Product("ResBaz", education, cer, "The Research Bazaar is a (free-of-charge) 3-day intensive festival and conference where researchers come together to up-skill in next generation digital research tools and scholarship.", ""));
            add(new Product("Winter Bootcamp", education, cer, "Winter Bootcamp is a week long training and education event co-ordinated by the Center for eResearch where you can up-skill in a whole host of topics related to digital research practice.", ""));
        }});

        // fetch all categories
        for (ProductCategory productCategory : productCategoryRepository.findAll()) {
            logger.info(productCategory.toString());
        }
    }
}
