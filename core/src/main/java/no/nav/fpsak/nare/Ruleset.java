package no.nav.fpsak.nare;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import no.nav.fpsak.nare.specification.ComputationalIfSpecification;
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

    /**
     * Realiserer flyten if <betingelse> then <then-spesifikasjon> else <else-spesifikasjon> for beregningsregel
     * @param testSpec
     * @param thenSpec
     * @param elseSpec
     * @return
     */
    public Specification<V> beregningHvisRegel(Specification<V> testSpec, Specification<V> thenSpec, Specification<V> elseSpec) {
        return new ComputationalIfSpecification<>(testSpec, thenSpec, elseSpec);
    }

    /**
     * Realiserer sekvens av to spesifikasjoner der bare den siste har betydning for videre flyt
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

    /**
     * Realiserer sekvens der en regel utføres N ganger, etterfulgt av en spesifikasjon som har betydning for videre flyt
     * @param id
     * @param beskrivelse
     * @param spec1 regel som utføres 1 gang for hvert element i argumentlisten, med argumentbeskrivelse + argumentverdi som serviceargument
     * @param regelmodell
     * @param argumentBeskrivelse
     * @param args1 argumentlisten, inneholder N argumenter
     * @param spec2 utføres 1 gang etter at spec1 er utført N ganger
     * @return
     */
    public Specification<V> beregningsRegel(String id, String beskrivelse, Class<? extends DynamicRuleService<V>> spec1, V regelmodell, String argumentBeskrivelse, List<? extends Object> args1, Specification<V> spec2) {
        Objects.requireNonNull(spec1, "spec1");
        Objects.requireNonNull(args1, "args1");
        Objects.requireNonNull(spec2, "spec2");
        if (args1.isEmpty()) {
            throw new IllegalArgumentException("Argumentlisten kan ikke være tom");
        }
        List<Specification<V>> specs = new ArrayList<>();
        args1.forEach(arg -> {
            DynamicRuleService<V> ny;
            try {
                ny = opprettSpesifikasjon(spec1, regelmodell);
                ny.medServiceArgument(new ServiceArgument(argumentBeskrivelse, arg));
                specs.add(ny.getSpecification());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException("Utviklerfeil: DynamicRuleService mangler public default constructor");
            }
        });
        specs.add(spec2);
        return new SequenceSpecification<V>(id, beskrivelse, specs);
    }

    private DynamicRuleService<V> opprettSpesifikasjon(Class<? extends DynamicRuleService<V>> spec1, V regelmodell) throws InstantiationException, IllegalAccessException {
        DynamicRuleService<V> ny = spec1.newInstance();
        ny.setRegelmodell(regelmodell);
        return ny;
    }
}
