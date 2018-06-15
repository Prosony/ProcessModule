package server;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Scanner;

public class Main {

    private final static int PORT = 8000;

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);

        URI webResourceBase = findWebResourceBase();
        System.err.println("Using BaseResource: " + webResourceBase);
        WebAppContext context = new WebAppContext();
        context.setBaseResource(Resource.newResource(webResourceBase));
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/classes/.*");
        context.setConfigurations(new Configuration[] {
                        new AnnotationConfiguration(),
                        new WebInfConfiguration(),
                        new WebXmlConfiguration(),
                        new MetaInfConfiguration(),
                        new FragmentConfiguration(),
                        new EnvConfiguration(),
                        new PlusConfiguration(),
                        new JettyWebXmlConfiguration()
                });
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        server.setHandler(context);
        server.start();
        server.dump(System.err);
        server.join();
    }

    private static URI findWebResourceBase() { //ClassLoader classLoader

        String fileName = "webapp/WEB-INF/web.xml";

        //Get file from resources folder
        ClassLoader classLoader = Main.class.getClassLoader();
        try {
            // Look for resource in classpath (best choice when working with archive jar/war file)
            URL webXml = classLoader.getResource(fileName);
            if (webXml != null) {
                URI uri = webXml.toURI().resolve("..").normalize();
                System.err.printf("WebResourceBase (Using ClassLoader reference) %s%n", uri);
                return uri;
            }
        }
        catch (URISyntaxException e) {
            throw new RuntimeException("Bad ClassPath reference for: " + fileName,e);
        }

        return null;
    }
}
