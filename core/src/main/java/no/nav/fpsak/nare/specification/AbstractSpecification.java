package no.nav.fpsak.nare.specification;

import com.google.gson.GsonBuilder;

import no.nav.fpsak.nare.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.DetailReasonKey;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.SingleEvaluation;

public abstract class AbstractSpecification<T> implements Specification<T> {
    protected String beskrivelse = "";
    protected String id = "";

    protected AbstractSpecification() {
    }

    protected AbstractSpecification(String id) {
        this.id = id;
    }

    @Override
    public String beskrivelse() {
        return beskrivelse;
    }

    @Override
    public Specification<T> eller(final Specification<T> specification) {
        return new OrSpecification<T>(this, specification);
    }

    @Override
    public String identifikator() {
        if (id.isEmpty()) {
            return Integer.toString(this.hashCode());
        } else {
            return id;
        }
    }

    public Evaluation ja(DetailReasonKey reasonKey, Object... reasonArgs) {
        return new SingleEvaluation(Resultat.JA, identifikator(), beskrivelse(), reasonKey, reasonArgs);
    }

    public Evaluation manuell(DetailReasonKey reasonKey, Object... reasonArgs) {
        return new SingleEvaluation(Resultat.MANUELL_BEHANDLING, identifikator(), beskrivelse(), reasonKey, reasonArgs);
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

    public Evaluation nei(DetailReasonKey reasonKey, Object... reasonArgs) {
        return new SingleEvaluation(Resultat.NEI, identifikator(), beskrivelse(), reasonKey, reasonArgs);
    }

    @Override
    public Specification<T> og(final Specification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    @Override
    public RuleDescription ruleDescription() {
        return new RuleDescription(Operator.SINGLE, identifikator(), beskrivelse());
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
