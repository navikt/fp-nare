package no.nav.aura.nare.specifications;


import no.nav.aura.nare.evalulation.Evaluation;
import no.nav.aura.nare.evalulation.Resultat;
import no.nav.aura.nare.evalulation.SingleEvaluation;

public abstract class AbstractSpecification<T> implements Specification<T> {

    protected AbstractSpecification(){}

    public Specification<T> og(final Specification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    public Specification<T> eller(final Specification<T> specification) {
        return new OrSpecification<T>(this, specification);
    }

    public Evaluation ja(String reason, Object... stringformatArguments){
        return new SingleEvaluation(Resultat.JA, identifikator(),beskrivelse(), reason, stringformatArguments);
    }

    public Evaluation nei(String reason, Object... stringformatArguments){
        return  new SingleEvaluation(Resultat.NEI, identifikator(), beskrivelse(), reason, stringformatArguments);
    }

    public Evaluation manuell(String reason, Object... stringformatArguments){
        return  new SingleEvaluation(Resultat.MANUELL_BEHANDLING, identifikator(), beskrivelse(), reason, stringformatArguments);
    }

}
