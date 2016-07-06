package no.nav.aura.nare.specifications.common;


import no.nav.aura.nare.Reason;
import no.nav.aura.nare.Resultat;

public abstract class AbstractSpecification<T> implements Specification<T> {

    protected AbstractSpecification(){}

    public Specification<T> og(final Specification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    public Specification<T> eller(final Specification<T> specification) {
        return new OrSpecification<T>(this, specification);
    }

    public Evaluation ja(String reason, Object... stringformatArguments){
        return  new Evaluation(Resultat.JA, identifikator(), beskrivelse(), reason, stringformatArguments);
    }

    public Evaluation nei(String reason, Object... stringformatArguments){
        return  new Evaluation(Resultat.NEI, identifikator(), beskrivelse(), reason, stringformatArguments);
    }

    public Evaluation manuell(String reason, Object... stringformatArguments){
        return  new Evaluation(Resultat.MANUELL_BEHANDLING, identifikator(), beskrivelse(), reason, stringformatArguments);
    }

    public Evaluation ja(Reason reason){
        return  new Evaluation(Resultat.JA, identifikator(), beskrivelse(), reason);
    }

    public Evaluation nei(Reason reason){
        return  new Evaluation(Resultat.NEI, identifikator(), beskrivelse(), reason);
    }

    public Evaluation manuell(Reason reason){
        return  new Evaluation(Resultat.MANUELL_BEHANDLING, identifikator(), beskrivelse(), reason);
    }

}
