package no.nav.aura.nare.input;

import java.util.Optional;
import java.util.stream.Stream;

public class Soknad implements Soker {

    public static Soknad adopsjonsSøknada(Person hovedsøker) {
        return new Soknad(hovedsøker, Soknadstype.ADOPSJON);
    }

    public static Soknad fodselSøknad(Person hovedsøker) {
        return new Soknad(hovedsøker, Soknadstype.FODSEL);
    }

    private Person barn;
    private Person hovedsøker;

    private Person medsøker;

    public String name = "Familien!";

    public Soknadstype soknadstype;

    private Soknad(Person hovedsøker, Soknadstype soknadstype) {
        this.hovedsøker = hovedsøker;
        this.soknadstype = soknadstype;
    }

    public Soknad forBarn(Person barn) {
        this.barn = barn;
        return this;
    }

    public Person getHovedsøker() {
        return hovedsøker;
    }

    public Person getMedsøker() {
        return medsøker;
    }

    public String getName() {
        return name;
    }

    public Soknadstype getSoknadstype() {
        return soknadstype;
    }

    public Optional<Person> getSøker(Rolle rolle) {
        return Stream.of(hovedsøker, medsøker).filter(person -> person.getRolle().equals(rolle)).findFirst();
    }

    public Soknad medSøker(Person medsøker) {
        this.medsøker = medsøker;
        return this;
    }
}
