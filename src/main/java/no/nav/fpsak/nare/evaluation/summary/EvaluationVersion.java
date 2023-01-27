package no.nav.fpsak.nare.evaluation.summary;

import java.util.Objects;

public class EvaluationVersion {
    private String name;
    private String version;

    public EvaluationVersion(String name, String version) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(name, "version");
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
