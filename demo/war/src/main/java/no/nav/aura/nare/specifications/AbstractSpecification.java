package no.nav.aura.nare.specifications;


import com.google.gson.GsonBuilder;
import no.nav.aura.nare.RuleDescription;
import no.nav.aura.nare.evaluation.Evaluation;
import no.nav.aura.nare.evaluation.Resultat;
import no.nav.aura.nare.evaluation.SingleEvaluation;

public abstract class AbstractSpecification<T> implements Specification<T> {
    protected String beskrivelse="";
    protected String id="";

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
    public RuleDescription ruleDescription() {
        return new RuleDescription(identifikator(),beskrivelse());
    }

    @Override
    public String identifikator() {
        if (id.isEmpty()) {
            return Integer.toString(this.hashCode());
        } else {
            return id;
        }
    }

    @Override
    public String beskrivelse(){
        return beskrivelse;
    }

    @Override
    public Specification medBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
        return this;
    }

    @Override
    public Specification medID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        System.out.println(this.beskrivelse());
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
