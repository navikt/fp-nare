package no.nav.fpsak.nare.specification;

public abstract class LeafSpecification<T> extends AbstractSpecification<T> {
    
    public LeafSpecification(String id) {
        super(id);
    }

    public LeafSpecification(String id, String beskrivelse) {
        this(id);
        medBeskrivelse(beskrivelse);
    }

}
