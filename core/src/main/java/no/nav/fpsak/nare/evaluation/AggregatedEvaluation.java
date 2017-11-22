package no.nav.fpsak.nare.evaluation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import no.nav.fpsak.nare.evaluation.summary.EvaluationVisitor;

public abstract class AggregatedEvaluation implements Evaluation {

    private String ruleIdentification;
    private String ruleDescription;
    private Operator operator;

    @SuppressWarnings("unused")
    private Resultat resultat;

    @SuppressWarnings("unused")
    private String reason;

    private Properties evaluationProperties;

    private List<Evaluation> children;

    protected AggregatedEvaluation(Operator operator, String id, String ruleDescription, Evaluation... children) {
        this.operator = operator;
        this.children = Arrays.asList(children);
        this.ruleIdentification = id;
        this.ruleDescription = ruleDescription;
        this.resultat = result();
        this.reason = reason();
    }

    public List<Evaluation> children() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String ruleDescription() {
        return ruleDescription;
    }

    @Override
    public String ruleIdentification() {
        return ruleIdentification;
    }

    @Override
    public void visit(Evaluation parentEvaluation, EvaluationVisitor visitor) {
        boolean visited = visitor.visiting(operator, parentEvaluation, this);
        if (!visited) {
            return; // allerede besøkt tidligere
        }
        visitChildren(visitor);
    }

    protected void visitChildren(EvaluationVisitor visitor) {
        for (Evaluation child : children) {
            child.visit(this, visitor); // NOSONAR
        }
    }

    protected Evaluation first() {
        return children.get(0);
    }

    protected Operator getOperator() {
        return operator;
    }

    protected Evaluation second() {
        return children.get(1);
    }
    
    @Override
    public RuleReasonRef getOutcome() {
        // definers kun i løvnoder
        return null;
    }

    @Override
    final public Properties getEvaluationProperties() {
        return evaluationProperties;
    }

    @Override
    final public void setEvaluationProperty(String key, String value) {
        if (evaluationProperties == null) {
            evaluationProperties = new Properties();
        }
        evaluationProperties.setProperty(key, value);
    }
}
