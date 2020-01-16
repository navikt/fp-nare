package no.nav.fpsak.nare.specification;

import java.util.Map;

import no.nav.fpsak.nare.doc.JsonOutput;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.RuleReasonRef;
import no.nav.fpsak.nare.evaluation.node.SingleEvaluation;

public abstract class AbstractSpecification<T> implements Specification<T> {
    private String beskrivelse = "";
    private String id = "";

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
        return new OrSpecification<>(this, specification);
    }

    @Override
    public String identifikator() {
        return id;
    }

    /** Ubegrunnet ja. */
    public SingleEvaluation ja() {
        return new SingleEvaluation(Resultat.JA, identifikator(), beskrivelse(), null);
    }

    public SingleEvaluation ja(RuleReasonRef reasonKey, Object... reasonArgs) {
        return new SingleEvaluation(Resultat.JA, identifikator(), beskrivelse(), reasonKey, reasonArgs);
    }

    public SingleEvaluation kanIkkeVurdere(RuleReasonRef reasonKey, Object... reasonArgs) {
        return new SingleEvaluation(Resultat.IKKE_VURDERT, identifikator(), beskrivelse(), reasonKey, reasonArgs);
    }

    /** Ubegrunnet nei. */
    public SingleEvaluation nei() {
        return new SingleEvaluation(Resultat.NEI, identifikator(), beskrivelse(), null);
    }

    public SingleEvaluation nei(RuleReasonRef reasonKey, Object... reasonArgs) {
        return new SingleEvaluation(Resultat.NEI, identifikator(), beskrivelse(), reasonKey, reasonArgs);
    }

    /** For beregningsregel: beregnet med mellomresultater. */
    public SingleEvaluation beregnet(Map<String, Object> mellomresultater) {
        SingleEvaluation resultat = ja();
        resultat.setEvaluationProperties(mellomresultater);
        return resultat;
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
    public Specification<T> og(final Specification<T> specification) {
        return new AndSpecification<>(this, specification);
    }

    @Override
    public RuleDescription ruleDescription() {
        return new SpecificationRuleDescription(Operator.SINGLE, identifikator(), beskrivelse());
    }

    @Override
    public String toString() {
        return JsonOutput.asJson(this);
    }
}
