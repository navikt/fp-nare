package no.nav.aura.nare.specifications;


import no.nav.aura.nare.RuleDescription;
import no.nav.aura.nare.evaluation.*;
import no.nav.aura.nare.evaluation.booleans.AndEvaluation;

/**
 * AND specification, used to create a new specifcation that is the AND of two other specifications.
 */
public class AndSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;
    private Specification<T> spec2;


    public AndSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    public Evaluation evaluate(final T t) {
        return new AndEvaluation(identifikator(), beskrivelse(), spec1.evaluate(t), spec2.evaluate(t));
    }

    @Override
    public String identifikator() {
        if (id.isEmpty()) {
            return "(" + spec1.identifikator() + " OG " + spec2.identifikator() +")";
        } else {
            return id;
        }
    }

    @Override
    public String beskrivelse() {
        if (beskrivelse.isEmpty()){
            return "(" + spec1.beskrivelse() + " OG " +  spec2.beskrivelse() + ")";
        }else{
            return beskrivelse;
        }
    }

    @Override
    public RuleDescription ruleDescription() {
        return new RuleDescription(Operator.AND, identifikator(), beskrivelse(), spec1.ruleDescription(),spec2.ruleDescription());
    }

}
