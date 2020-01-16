package no.nav.fpsak.nare;

import no.nav.fpsak.nare.evaluation.Evaluation;

public abstract class DynamicRuleService<T> implements RuleService<T> {

    protected T regelmodell;
    private ServiceArgument serviceArgument;

    protected DynamicRuleService() {
        super();
    }

    public DynamicRuleService(T regelmodell) {
        this();
        this.regelmodell = regelmodell;
    }

    public DynamicRuleService<T> medServiceArgument(ServiceArgument serviceArgument) {
        this.serviceArgument = serviceArgument;
        return this;
    }

    public ServiceArgument getServiceArgument() {
        return serviceArgument;
    }

    void setRegelmodell(T regelmodell) {
        this.regelmodell = regelmodell;
    }

    @Override
    public final Evaluation evaluer(T regelmodell) {
        setRegelmodell(regelmodell);
        Evaluation evaluation = getSpecification().evaluate(regelmodell);
        if (serviceArgument != null) {
            evaluation.setEvaluationProperty(serviceArgument.getBeskrivelse(), serviceArgument.getVerdi());
        }
        return evaluation;
    }
}
