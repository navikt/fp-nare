package no.nav.fpsak.nare.evaluation.summary;

import java.util.Properties;

public class NareVersion {

    private NareVersion() {
    }

    public static final EvaluationVersion NARE_VERSION = readVersionPropertyFor("nare", "nare/nare-version.properties");


    public static EvaluationVersion readVersionPropertyFor(String projectName, String propertiesFile) {
        String version;
        try {
            final Properties properties = new Properties();
            properties.load(NareVersion.class.getClassLoader().getResourceAsStream(propertiesFile));
            version = properties.getProperty("version");
        } catch (Exception e) {
            version = "UNKNOWN";
        }
        return new EvaluationVersion(projectName, version);
    }

}
