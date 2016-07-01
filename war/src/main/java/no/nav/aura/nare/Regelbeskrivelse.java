package no.nav.aura.nare;


import java.util.Objects;

public class Regelbeskrivelse implements Comparable<Regelbeskrivelse> {

    private final Integer id;
    private String beskrivelse;

    private Regelbeskrivelse(final Integer id){
        this.id = id;
    }

    public static Regelbeskrivelse id(int id){
        return new Regelbeskrivelse(id);
    }

    public Regelbeskrivelse beskrivelse (String beskrivelse){
        this.beskrivelse = beskrivelse;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Regelbeskrivelse that = (Regelbeskrivelse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(beskrivelse, that.beskrivelse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beskrivelse);
    }


    @Override
    public String toString() {
        return "Regel " + id +
                ": " + beskrivelse + '\n';
    }

    @Override
    public int compareTo(Regelbeskrivelse other) {
        return id.compareTo(other.id);
    }
}
