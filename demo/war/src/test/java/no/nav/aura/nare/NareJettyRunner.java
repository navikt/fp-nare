package no.nav.aura.nare;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class NareJettyRunner {

    private static final String WEB_SRC = "src/main/webapp";
    protected Server server;


    public NareJettyRunner(int port, String overrideDescriptor) {
        server = new Server(port);
        server.setHandler(getContext(overrideDescriptor));
    }


    public static void main(String[] args) throws Exception {
        NareJettyRunner jetty = new NareJettyRunner(1337, new File(getProjectRoot(), "src/test/resources/override-web.xml").getPath());
        jetty.start();
        jetty.server.join();
    }

    public static File getProjectRoot() {
        File path = new File(NareJettyRunner.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        while (!new File(path, "target").exists()) {
            path = path.getParentFile();
        }
        return path;
    }


    public WebAppContext getContext(String overrideDescriptor) {

        final WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setResourceBase(setupResourceBase());
        Configuration[] configurations = {new WebXmlConfiguration(), new WebInfConfiguration()};
        context.setConfigurations(configurations);
        if (overrideDescriptor != null) {
            context.setOverrideDescriptor(overrideDescriptor);
        }
        return context;
    }

    private String setupResourceBase() {
        try {
            File file = new File(getClass().getResource("/override-web.xml").toURI());
            File projectDirectory = file.getParentFile().getParentFile().getParentFile();
            File webappDir = new File(projectDirectory, WEB_SRC);
            return webappDir.getCanonicalPath();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Could not find webapp directory", e);
        }
    }

    public WebApplicationContext getSpringContext() {
        WebAppContext webApp = (WebAppContext) server.getHandler();
        return WebApplicationContextUtils.getWebApplicationContext(webApp.getServletContext());
    }



    public void start() {
        try {
            server.start();
            System.out.println("Jetty started on port " + getPort());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void stop() {
        try {
            server.stop();
            System.out.println("Jetty stopped");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public int getPort() {
        return ((ServerConnector) server.getConnectors()[0]).getLocalPort();
    }


}
