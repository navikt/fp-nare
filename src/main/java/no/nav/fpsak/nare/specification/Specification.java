package no.nav.fpsak.nare.specification;

import no.nav.fpsak.nare.ServiceArgument;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;

/**
 * Specificaiton interface.
 * <p/>
 * Use {@link AbstractSpecification} as base for creating specifications, og only the method {@link #evaluate(Object)}
 * must be
 * implemented.
 */
public interface Specification<T> {

    // ID og beskrivelse av regel
    String identifikator();

    String beskrivelse();

    /**
     * Check if {@code t} is satisfied by the specification.
     *
     * @param t Object to test.
     * @return {@code true} if {@code t} satisfies the specification.
     */
    Evaluation evaluate(T t);

    /**
     * Specification Builder methods
     */
    Specification<T> medBeskrivelse(String beskrivelse);

    Specification<T> medID(String id);

    /**
     * Setter en evaluationproperty basert på beskrivelse og objekt i ServiceArgument.
     * Er implementert av enkelte aggregate-specifications for å sette en property for samleevalueringen.
     */
    default Specification<T> medEvaluationProperty(ServiceArgument property) {
        return this;
    }

    /**
     * Create a new specification that is the AND operation of {@code this} specification of another specification.
     *
     * @param specification Specification to AND.
     * @return A new specification.
     */
    Specification<T> og(Specification<T> specification);

    /**
     * Create a new specification that is the OR operation of {@code this} specification of another specification.
     *
     * @param specification Specification to OR.
     * @return A new specification.
     */
    Specification<T> eller(Specification<T> specification);

    // For produksjon av regelgraf
    RuleDescription ruleDescription();
}
