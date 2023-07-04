package no.nav.fpsak.nare.evaluation.summary;

import java.util.Objects;

public record EvaluationVersion(String name, String version) {

    public EvaluationVersion {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(version, "version");
    }

    @Deprecated(forRemoval = true, since = "2.6.0")
    public String getName() {
        return name;
    }

    @Deprecated(forRemoval = true, since = "2.6.0")
    public String getVersion() {
        return version;
    }
}
