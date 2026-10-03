package com.unifranz.sistemaalquilerinstrumentos.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Solo con el perfil "https": abre un conector HTTP en el puerto 8080 que redirige
 * automaticamente todo el trafico al puerto HTTPS (server.port = 8443).
 */
@Configuration
@Profile("https")
public class HttpsRedirectConfig {

    @Value("${server.port}")
    private int puertoHttps;

    @Value("${app.https.puerto-http:8080}")
    private int puertoHttp;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint restriccion = new SecurityConstraint();
                restriccion.setUserConstraint("CONFIDENTIAL");
                SecurityCollection coleccion = new SecurityCollection();
                coleccion.addPattern("/*");
                restriccion.addCollection(coleccion);
                context.addConstraint(restriccion);
            }
        };
        tomcat.addAdditionalTomcatConnectors(conectorHttp());
        return tomcat;
    }

    private Connector conectorHttp() {
        Connector conector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        conector.setScheme("http");
        conector.setPort(puertoHttp);
        conector.setSecure(false);
        conector.setRedirectPort(puertoHttps);
        return conector;
    }
}
