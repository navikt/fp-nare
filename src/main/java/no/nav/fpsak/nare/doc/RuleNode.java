package no.nav.fpsak.nare.doc;

import no.nav.fpsak.nare.evaluation.Operator;

@SuppressWarnings("unused")
public record RuleNode(String id, String ruleId, String ruleDescription,  Operator operator, RuleDescription rule) {

    public RuleNode(String id, String ruleId, String ruleDescription, Operator operator) {
        this(id, ruleId, ruleDescription, operator, null);
    }

    public RuleNode(String id, RuleDescription node) {
        this(id, node.getRuleIdentification(), node.getRuleDescription(), node.getOperator(), node);
    }

}