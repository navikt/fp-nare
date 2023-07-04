package no.nav.fpsak.nare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.nav.fpsak.nare.specification.ComputationalIfSpecification;
import no.nav.fpsak.nare.specification.ConditionalOrSpecification;
import no.nav.fpsak.nare.specification.SequenceSpecification;
import no.nav.fpsak.nare.specification.Specification;

public class Ruleset<V> {

    public Specification<V> regel(String id, Specification<V> specification) {
        return specification.medID(id);
    }

    public Specification<V> regel(String id, String beskrivelse, Specification<V> specification) {
        return specification.medBeskrivelse(beskrivelse).medID(id);
    }

    public ConditionalOrSpecification.Builder<V> hvisRegel() {
        return ConditionalOrSpecification.<V>regel();
    }

    public ConditionalOrSpecification.Builder<V> hvisRegel(String id, String beskrivelse) {
        return ConditionalOrSpecification.<V>regel(id, beskrivelse);
    }

    public SequenceSpecification.Builder<V> sekvensRegel() {
        return SequenceSpecification.<V>regel();
    }

    public SequenceSpecification.Builder<V> sekvensRegel(String id, String beskrivelse) {
        return SequenceSpecification.<V>regel(id, beskrivelse);
    }

    public ComputationalIfSpecification.Builder<V> dersomRegel(Specification<V> ifSpec) {
        return ComputationalIfSpecification.<V>regel(ifSpec);
    }

    /**
     * Realiserer flyten if <betingelse> then <then-spesifikasjon> else <else-spesifikasjon> for beregningsregel
     * 
     * @param testSpec
     * @param thenSpec
     * @param elseSpec
     * @return
     */
    public Specification<V> beregningHvisRegel(Specification<V> testSpec, Specification<V> thenSpec, Specification<V> elseSpec) {
        return new ComputationalIfSpecification<>(testSpec, thenSpec, elseSpec);
    }

    /**
     * Realiserer flyten if <betingelse> then <then-spesifikasjon> (else true) for beregningsregel
     *
     * @param testSpec
     * @param thenSpec
     * @return
     */
    public Specification<V> beregningHvisRegel(Specification<V> testSpec, Specification<V> thenSpec) {
        return new ComputationalIfSpecification<>(testSpec, thenSpec);
    }

    /**
     * Realiserer sekvens av 2 spesifikasjoner der bare den siste har betydning for videre flyt
     *
     * @param id
     * @param beskrivelse
     * @param spec1
     * @param spec2
     * @return
     */
    public Specification<V> beregningsRegel(String id, String beskrivelse, Specification<V> spec1, Specification<V> spec2) {
        return new SequenceSpecification<>(id, beskrivelse, spec1, spec2);
    }

    /**
     * Realiserer sekvens av N spesifikasjoner der bare den siste har betydning for videre flyt
     * 
     * @param id
     * @param beskrivelse
     * @param specs
     * @return
     */
    public Specification<V> beregningsRegel(String id, String beskrivelse, Specification<V>... specs) {
        return new SequenceSpecification<>(id, beskrivelse, specs);
    }

    /**
     * Realiserer sekvens av N spesifikasjoner der bare den siste har betydning for videre flyt
     *
     * @param specs
     * @return
     */
    public Specification<V> beregningsRegel(Specification<V>... specs) {
        return new SequenceSpecification<>(Arrays.asList(specs));
    }

    /**
     * Realiserer sekvens av N spesifikasjoner der bare den siste har betydning for videre flyt
     * 
     * @param id
     * @param beskrivelse
     * @param spec1list
     * @param spec2
     * @return
     */
    public Specification<V> beregningsRegel(String id, String beskrivelse, List<Specification<V>> spec1list, Specification<V> spec2) {
        List<Specification<V>> specs = new ArrayList<>();
        specs.addAll(spec1list);
        specs.add(spec2);
        return new SequenceSpecification<>(id, beskrivelse, specs);
    }
}
