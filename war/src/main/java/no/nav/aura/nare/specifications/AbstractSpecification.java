package no.nav.aura.nare.specifications;


import com.google.gson.GsonBuilder;
import no.nav.aura.nare.evaluation.Evaluation;
import no.nav.aura.nare.evaluation.Resultat;
import no.nav.aura.nare.evaluation.SingleEvaluation;

public abstract class AbstractSpecification<T> implements Specification<T> {


    protected AbstractSpecification(){
    }

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

    @Override
    public String toString() {
        System.out.println(this.beskrivelse());
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
