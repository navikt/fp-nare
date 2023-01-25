package no.nav.fpsak.nare.doc;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.fpsak.nare.evaluation.Operator;

@SuppressWarnings("unused")
public class RuleNode {
    final String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String ruleId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String ruleDescription;
    private final Operator operator;
    private RuleDescription rule;

    @Deprecated(forRemoval = true, since = "2.5.0")
    public RuleNode(String ruleId, String ruleDescription, Operator operator) {
        this.id = UUID.randomUUID().toString();
        this.ruleId = ruleId;
        this.ruleDescription = ruleDescription;
        this.operator = operator;
    }

    @Deprecated(forRemoval = true, since = "2.5.0")
    public RuleNode(RuleDescription node) {
        this.id = UUID.randomUUID().toString();
        this.ruleId = node.getRuleIdentification();
        this.ruleDescription = node.getRuleDescription();
        this.operator = node.getOperator();
        this.rule = node;
    }

    public RuleNode(String id, String ruleId, String ruleDescription, Operator operator) {
        this.id = id;
        this.ruleId = ruleId;
        this.ruleDescription = ruleDescription;
        this.operator = operator;
    }

    public RuleNode(String id, RuleDescription node) {
        this.id = id;
        this.ruleId = node.getRuleIdentification();
        this.ruleDescription = node.getRuleDescription();
        this.operator = node.getOperator();
        this.rule = node;
    }

}