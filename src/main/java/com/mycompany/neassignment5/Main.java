/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.neassignment5;
import java.io.File;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 *
 * @author Nate
 */
public class Main {
    public static void main(String[] args) throws Exception {

        // Initialize Tomcat on a given Port
        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();

        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8888";
        }
        tomcat.setPort(Integer.valueOf(webPort));

        // Setup the basic Web Context at / based on webappDirLocation above
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());

        // Add the ApplicationConfig as a new Resource
        ResourceConfig resCfg = new ResourceConfig(new ApplicationConfig().getClasses());
        // Register the Jackson Feature to allow JSON transcoding
        resCfg.register(org.glassfish.jersey.jackson.JacksonFeature.class);
        // Setup the RESTful Application at /api
        ServletContainer srvCtr = new ServletContainer(resCfg);
        Tomcat.addServlet(ctx, "jersey-container-servlet", srvCtr);
        ctx.addServletMapping("/api/*", "jersey-container-servlet");
        
        System.out.println("Listening on http://localhost:" + webPort);

        // Start the server, and let it listen
        tomcat.start();
        tomcat.getServer().await();
    }
    
}
