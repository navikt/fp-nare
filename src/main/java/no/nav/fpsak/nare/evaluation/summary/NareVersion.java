package no.nav.fpsak.nare.evaluation.summary;

import java.util.Properties;

public class NareVersion {

    private NareVersion() {
    }

    public static final EvaluationVersion NARE_VERSION = readProjectProperties(NareVersion.class.getClassLoader(), "nare");


    public static EvaluationVersion readProjectProperties(ClassLoader cl, String defaultProjectName) {
        String name;
        String version;
        try {
            final Properties properties = new Properties();
            properties.load(cl.getResourceAsStream("project.properties"));
            name = properties.getProperty("name");
            version = properties.getProperty("version");
        } catch (Exception e) {
            name = defaultProjectName;
            version = "0.0.0";
        }
        return new EvaluationVersion(name, version);
    }

}
