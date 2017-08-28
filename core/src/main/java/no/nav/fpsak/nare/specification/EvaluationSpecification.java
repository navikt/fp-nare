package no.nav.fpsak.nare.specification;

import no.nav.fpsak.nare.evaluation.Evaluation;

/**
 * Single method interface slik også kan brukes for Lambdas.
 */
public interface EvaluationSpecification<T> {
    /**
     * Check if {@code t} is satisfied by the specification.
     * 
     * @param t
     *            Object to test.
     * @return {@code true} if {@code t} satisfies the specification.
     */
    Evaluation evaluate(T t);

}