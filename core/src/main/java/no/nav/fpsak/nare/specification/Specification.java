package no.nav.fpsak.nare.specification;


import no.nav.fpsak.nare.RuleDescription;

/**
 * Specificaiton interface.
 * <p/>
 * Use {@link AbstractSpecification} as base for creating specifications, og only the method {@link #evaluate(Object)} must be
 * implemented.
 */
public interface Specification<T> extends EvaluationSpecification<T> {

    /**
     * Create a new specification that is the AND operation of {@code this} specification of another specification.
     * 
     * @param specification
     *            Specification to AND.
     * @return A new specification.
     */
    Specification<T> og(Specification<T> specification);

    /**
     * Create a new specification that is the OR operation of {@code this} specification of another specification.
     * 
     * @param specification
     *            Specification to OR.
     * @return A new specification.
     */
    Specification<T> eller(Specification<T> specification);

    String identifikator();

    String beskrivelse();

    RuleDescription ruleDescription();

    Specification<T>  medBeskrivelse(String beskrivelse);

    Specification<T>  medID(String id);
}
