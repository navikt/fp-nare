package no.nav.fpsak.nare.evaluation.summary;

import java.util.Objects;

public record EvaluationVersion(String name, String version) {

    public EvaluationVersion {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(version, "version");
    }

    public String nameAndVersion() {
        return name() + ':' + version();
    }
}
