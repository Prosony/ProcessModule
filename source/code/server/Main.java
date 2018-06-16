package server;

import debug.LocalLog;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.*;
import services.config.GetConfig;

import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class Main {


    private static int PORT;
    private static  LocalLog LOG = LocalLog.getInstance();
    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String globalConfigPath = rootPath + "global-config.properties";
        properties.load(new FileInputStream(globalConfigPath));
        PORT = Integer.valueOf(properties.getProperty("PORT"));
        Server server = new Server(PORT);

        URI webResourceBase = findWebResourceBase();
        System.err.println("Using BaseResource: " + webResourceBase);
        WebAppContext context = new WebAppContext();
        if (webResourceBase != null){
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
            LOG.info("\u001B[32m"+"[SUCCESS]"+ "\u001B[0m" +" server started on port: "+PORT);
            server.join();
        }else{
            LOG.error("Unable to find web resource ref: webapp/WEB-INF/web.xml");
        }
    }

    private static URI findWebResourceBase() {  //ClassLoader classLoader
        String fileName = "webapp/WEB-INF/web.xml";
        ClassLoader classLoader = Main.class.getClassLoader();  //Get file from resources folder
        try {
            URL webXml = classLoader.getResource(fileName); // Look for resource in classpath (best choice when working with archive jar/war file)
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
