package nz.ac.auckland.cer.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
public class CorsConfig extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CorsConfig.class);

    @Value("${app.enable-cors}")
    private boolean enableCors;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (enableCors) {
            logger.warn("CORS is enabled: please only enable when testing locally");
            registry.addMapping("/**");
        }
    }
}