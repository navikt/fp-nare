package no.nav.fpsak.nare;

import java.util.Objects;

public class ServiceArgument {

    public ServiceArgument(String beskrivelse, Object verdi) {
        super();
        Objects.requireNonNull(beskrivelse, "beskrivelse");
        Objects.requireNonNull(verdi, "verdi");
        this.beskrivelse = beskrivelse;
        this.verdi = verdi;
    }

    private String beskrivelse;
    private Object verdi;

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public Object getVerdi() {
        return verdi;
    }

}
