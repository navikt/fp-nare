package no.nav.fpsak.nare;

import no.nav.fpsak.nare.specification.ConditionalOrSpecification;
import no.nav.fpsak.nare.specification.ConditionalOrSpecification.Builder;
import no.nav.fpsak.nare.specification.SequenceSpecification;
import no.nav.fpsak.nare.specification.Specification;

public class Ruleset<V> {

    public Specification<V> regel(String id, Specification<V> specification) {
        return specification.medID(id);
    }

    public Specification<V> regel(String id, String beskrivelse, Specification<V> specification) {
        return specification.medBeskrivelse(beskrivelse).medID(id);
    }

    public Builder<V> hvisRegel(String id, String beskrivelse) {
        return ConditionalOrSpecification.<V> regel(id, beskrivelse);
    }

    public Specification<V> beregningsRegel(Specification<V> spec1, Specification<V> spec2) {
        return new SequenceSpecification<>(spec1, spec2);
    }

}
