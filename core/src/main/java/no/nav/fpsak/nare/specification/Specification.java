package no.nav.fpsak.nare.specification;

import no.nav.fpsak.nare.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;

/**
 * Specificaiton interface.
 * <p/>
 * Use {@link AbstractSpecification} as base for creating specifications, og only the method {@link #evaluate(Object)}
 * must be
 * implemented.
 */
public interface Specification<T> {
    String beskrivelse();

    /**
     * Create a new specification that is the OR operation of {@code this} specification of another specification.
     *
     * @param specification
     *            Specification to OR.
     * @return A new specification.
     */
    Specification<T> eller(Specification<T> specification);

    /**
     * Check if {@code t} is satisfied by the specification.
     *
     * @param t
     *            Object to test.
     * @return {@code true} if {@code t} satisfies the specification.
     */
    Evaluation evaluate(T t);

    String identifikator();

    Specification<T> medBeskrivelse(String beskrivelse);

    Specification<T> medID(String id);

    /**
     * Create a new specification that is the AND operation of {@code this} specification of another specification.
     *
     * @param specification
     *            Specification to AND.
     * @return A new specification.
     */
    Specification<T> og(Specification<T> specification);

    RuleDescription ruleDescription();
    
    void visit(Specification<T> parentSpecification, SpecificationVisitor<T> visitor);
}
