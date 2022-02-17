package no.nav.fpsak.nare;

import java.util.Objects;

public record ServiceArgument(String beskrivelse, Object verdi) {

    public ServiceArgument {
        Objects.requireNonNull(beskrivelse, "beskrivelse");
        Objects.requireNonNull(verdi, "verdi");
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public Object getVerdi() {
        return verdi;
    }

}
