package no.nav.fpsak.nare.doc;

import java.util.UUID;

import no.nav.fpsak.nare.evaluation.Operator;

@SuppressWarnings("unused")
public class RuleNode {
    final String id = UUID.randomUUID().toString();

    private final String ruleId;
    private final String ruleDescription;
    private final Operator operator;

    private RuleDescription rule;

    public RuleNode(String ruleId, String ruleDescription, Operator operator) {
        super();
        this.ruleId = ruleId;
        this.ruleDescription = ruleDescription;
        this.operator = operator;
    }

    public RuleNode(RuleDescription node) {
        this.ruleId = node.getRuleIdentification();
        this.ruleDescription = node.getRuleDescription();
        this.operator = node.getOperator();
        this.rule = node;
    }

}