package nz.ac.auckland.cer.config;


import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Value("${tomcat.ajp.port}")
    int ajpPort;

    @Value("${tomcat.ajp.remote-authentication}")
    String remoteAuthentication;

    @Value("${tomcat.ajp.enabled}")
    boolean tomcatAjpEnabled;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcatEmbedded = new TomcatEmbeddedServletContainerFactory();

        if (tomcatAjpEnabled) {
            Connector connector = new Connector("AJP/1.3");
            connector.setPort(ajpPort);
            connector.setSecure(false);
            connector.setAllowTrace(false);
            connector.setScheme("http");
            tomcatEmbedded.addAdditionalTomcatConnectors(connector);
        }

        return tomcatEmbedded;
    }
}
