package no.nav.fpsak.nare;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import no.nav.fpsak.nare.specification.ComputationalIfSpecification;
import no.nav.fpsak.nare.specification.ConditionalOrSpecification;
import no.nav.fpsak.nare.specification.ForeachSpecification;
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
    public Specification<V> beregningHvisRegel(Specification<V> testSpec, Specification<V> thenSpec,
            Specification<V> elseSpec) {
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

    /**
     * Realiserer sekvens der en regel utføres N ganger - 1 gang for hvert element i en collection
     *
     * @param id
     * @param beskrivelse
     * @param spec          regel som utføres 1 gang for hvert element i argumentlisten, med argName + arg som "scope"
     * @param argName
     * @param args          argumentlisten, inneholder N argumenter
     * @return
     */
    public Specification<V> beregningsForeachRegel(String id, String beskrivelse, Specification<V> spec,
                                                   String argName, List<?> args) {
        Objects.requireNonNull(spec, "spec1");
        Objects.requireNonNull(args, "args1");
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Argumentlisten kan ikke være tom");
        }
        return new ForeachSpecification<>(id, beskrivelse, spec, args, argName);
    }

    /**
     * Realiserer sekvens der en regel utføres N ganger - 1 gang for hvert element i en collection
     *
     * @param id
     * @param beskrivelse
     * @param spec          regel som utføres 1 gang for hvert element i argumentlisten, med argName + arg som "scope"
     * @param argName
     * @param args          argumentlisten, inneholder N argumenter
     * @return
     */
    public Specification<V> beregningsForeachRegel(Specification<V> spec, String argName, List<?> args) {
        Objects.requireNonNull(spec, "spec1");
        Objects.requireNonNull(args, "args1");
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Argumentlisten kan ikke være tom");
        }
        return new ForeachSpecification<>(spec, args, argName);
    }

    /**
     * Realiserer sekvens der en regel utføres N ganger - 1 gang for hvert element i en collection - fulgt av annen regel
     *
     * @param id
     * @param beskrivelse
     * @param spec          regel som utføres 1 gang for hvert element i argumentlisten, med argName + arg som "scope"
     * @param argName
     * @param args          argumentlisten, inneholder N argumenter
     * @param specThen      regel som utføres 1 gang etter at spec er utført N ganger
     * @return
     */
    public Specification<V> beregningsForeachThenRegel(String id, String beskrivelse, Specification<V> spec,
                                                   String argName, List<?> args, Specification<V> specThen) {
        Objects.requireNonNull(spec, "spec");
        Objects.requireNonNull(args, "args1");
        Objects.requireNonNull(specThen, "specThen");
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Argumentlisten kan ikke være tom");
        }
        return new SequenceSpecification<>(id, beskrivelse, new ForeachSpecification<>(id, beskrivelse, spec, args, argName), specThen);
    }

    /**
     * Realiserer sekvens der en regel utføres N ganger, etterfulgt av en
     * spesifikasjon som har betydning for videre flyt
     * 
     * @param id
     * @param beskrivelse
     * @param spec1               regel som utføres 1 gang for hvert element i
     *                            argumentlisten, med argumentbeskrivelse +
     *                            argumentverdi som serviceargument
     * @param regelmodell
     * @param argumentBeskrivelse
     * @param args1               argumentlisten, inneholder N argumenter
     * @param spec2               utføres 1 gang etter at spec1 er utført N ganger
     * @return
     */
    @Deprecated // Konsturer en liste selv og bruk beregningsRegel(spec1list, spec2) eller Bruk beregningForeach eller beregningForeachThen
    public Specification<V> beregningsRegel(String id, String beskrivelse, Class<? extends DynamicRuleService<V>> spec1,
            V regelmodell, String argumentBeskrivelse, List<? extends Object> args1, Specification<V> spec2) {
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
                throw new IllegalStateException("Utviklerfeil: DynamicRuleService mangler public default constructor",
                        e);
            }
        });
        specs.add(spec2);
        return new SequenceSpecification<>(id, beskrivelse, specs);
    }

    private DynamicRuleService<V> opprettSpesifikasjon(Class<? extends DynamicRuleService<V>> spec1, V regelmodell)
            throws InstantiationException, IllegalAccessException {

        try {
            DynamicRuleService<V> ny = spec1.getDeclaredConstructor().newInstance();
            ny.setRegelmodell(regelmodell);
            return ny;
        } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new IllegalStateException("Utviklerfeil: DynamicRuleService mangler public default constructor", e);
        }

    }
}
