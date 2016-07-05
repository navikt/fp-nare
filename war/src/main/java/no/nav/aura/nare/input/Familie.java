package no.nav.aura.nare.input;


import java.util.Optional;
import java.util.stream.Stream;

public class Familie implements Soker {


    private Person barn;
    private Person hovedsoker;
    private Person medsoker;
    public String name = "Familien!";

    private Familie(Person hovedsoker){
        this.hovedsoker = hovedsoker;
    }

    public Person getHovedsoker() {
        return hovedsoker;
    }

    public Person getMedsoker() {
        return medsoker;
    }

    public String getName() {
        return name;
    }

    public Familie medSoker(Person medsoker){
        this.medsoker = medsoker;
        return this;
    }

    public static Familie hovedsoker(Person hovedsoker){
        return new Familie(hovedsoker);
    }

    public Familie forBarn(Person barn) {
        this.barn = barn;
        return this;
    }

    public Optional<Person> getSoker(Rolle rolle) {
        return Stream.of(hovedsoker, medsoker).filter(person -> person.getRolle().equals(rolle)).findFirst();
    }
}
