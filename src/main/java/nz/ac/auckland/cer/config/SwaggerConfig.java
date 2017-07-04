package nz.ac.auckland.cer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
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
        return new ApiInfo(
                "The Research Hub API",
                "A RESTful API for the Research Hub",
                "0.1.0",
                "",
                "James Diprose",
                "MIT",
                "https://raw.githubusercontent.com/UoA-eResearch/research-hub-api/mvp/LICENSE");
    }
}