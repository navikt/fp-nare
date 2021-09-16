package no.nav.fpsak.nare.evaluation;

import java.util.Map;
import java.util.TreeMap;

public abstract class BasicEvaluation implements Evaluation {
    private String ruleIdentification;
    private String ruleDescription;
    protected Resultat resultat;
    protected String reason;
    private Object output;

    private Map<String, Object> evaluationProperties;

    public BasicEvaluation(String ruleIdentification, String ruleDescription) {
        this.ruleIdentification = ruleIdentification;
        this.ruleDescription = ruleDescription;
    }

    @Override
    public String reason() {
        return reason;
    }

    @Override
    public Resultat result() {
        return resultat;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T output() {
        return (T)output;
    }

    @Override
    public String ruleDescriptionText() {
        return ruleDescription;
    }

    @Override
    public String ruleIdentification() {
        return ruleIdentification;
    }

    public void setEvaluationProperties(Map<String, Object> props) {
        this.evaluationProperties = props;
    }

    @Override
    public Map<String, Object> getEvaluationProperties() {
        return evaluationProperties;
    }

    @Override
    public void setEvaluationProperty(String key, Object value) {
        if (this.evaluationProperties == null) {
            this.evaluationProperties = new TreeMap<>();
        }
        this.evaluationProperties.put(key, value);
    }

    public void setOutput(Object output) {
        this.output = output;
    }

}
