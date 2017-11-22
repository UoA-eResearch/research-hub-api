package nz.ac.auckland.cer.config;

import org.apache.maven.model.Contributor;
import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .directModelSubstitute(LocalDate.class, String.class);
    }

    private ApiInfo apiInfo() {
        String title = "";
        String description = "";
        String version = "";
        String termsOfServiceUrl = "";
        Contact contact = new Contact("", "", "");
        String license = "";
        String licenseUrl = "";
        Collection<VendorExtension> vendorExtensions = new ArrayList<>();

        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader("pom.xml"));

            title = model.getName();
            description = model.getDescription();
            version = model.getVersion();
            termsOfServiceUrl = "";

            List<Contributor> contributors = model.getContributors();

            if (contributors.size() > 0) {
                Contributor contributor = contributors.get(0);
                contact = new Contact(contributor.getName(), contributor.getUrl(), contributor.getEmail());
            }

            List<License> licenses = model.getLicenses();

            if (licenses.size() > 0) {
                License firstLicense = licenses.get(0);
                license = firstLicense.getName();
                licenseUrl = firstLicense.getUrl();
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

        return new ApiInfo(title, description, version, termsOfServiceUrl, contact, license, licenseUrl, vendorExtensions);
    }
}