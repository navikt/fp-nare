package no.nav.fpsak.nare;

import no.nav.fpsak.nare.evaluation.Evaluation;

public abstract class DynamicRuleService<T> implements RuleService<T> {

    protected T regelmodell;

    protected DynamicRuleService() {
        super();
    }

    public DynamicRuleService(T regelmodell) {
        this();
        this.regelmodell = regelmodell;
    }

    @SuppressWarnings("unused")
    public DynamicRuleService<T> medArgument(Object argument) {
        return this;
    }

    void setRegelmodell(T regelmodell) {
        this.regelmodell = regelmodell;
    }

    @Override
    final public Evaluation evaluer(T regelmodell) {
        setRegelmodell(regelmodell);
        return getSpecification().evaluate(regelmodell);
    }
}
