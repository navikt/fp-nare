package no.nav.fpsak.nare.specification;


import com.google.gson.GsonBuilder;

import no.nav.fpsak.nare.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.SingleEvaluation;

public abstract class AbstractSpecification<T> implements Specification<T> {
    protected String beskrivelse="";
    protected String id="";

    protected AbstractSpecification(){
    }

    @Override
	public Specification<T> og(final Specification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    @Override
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
    public Specification<T> medBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
        return this;
    }

    @Override
    public Specification<T> medID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        System.out.println(this.beskrivelse());
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
