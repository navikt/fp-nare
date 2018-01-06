package no.nav.fpsak.nare.evaluation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.summary.EvaluationVisitor;

public abstract class AggregatedEvaluation extends BasicEvaluation {

    private final Operator operator;

    private final List<Evaluation> children;

    protected AggregatedEvaluation(Operator operator, String id, String ruleDescription, Evaluation... children) {
        super(id, ruleDescription);
        this.operator = operator;
        this.children = Arrays.asList(children);

        // disse er avhengig av at children er satt
        this.resultat = result();
        this.reason = reason();
    }

    public List<Evaluation> children() {
        return Collections.unmodifiableList(children);
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
    public RuleDescription toRuleDescription() {
        return new EvaluationRuleDescription(getOperator(), this, children());
    }

}
