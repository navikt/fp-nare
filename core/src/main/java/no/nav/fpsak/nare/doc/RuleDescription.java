package no.nav.fpsak.nare.doc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.gson.GsonBuilder;

import no.nav.fpsak.nare.evaluation.Operator;

public class RuleDescription {

    private final String ruleIdentification;
    private final String ruleDescription;
    private final Operator operator;
    private final List<RuleDescription> children;

    public RuleDescription(Operator operator, String ruleIdentification, String ruleDescription, RuleDescription... children) {
        // Objects.requireNonNull(operator, "operator");
        Objects.requireNonNull(ruleIdentification, "ruleIdentification");
        Objects.requireNonNull(ruleDescription, "ruleDescription");

        if (Objects.equals(Operator.SINGLE, operator) && children != null && children.length > 0) {
            throw new IllegalArgumentException("Cannot have a SINGLE node with children : " + children.length);
        }
        this.operator = operator;
        this.ruleIdentification = ruleIdentification;
        this.ruleDescription = ruleDescription;
        this.children = children == null || children.length == 0 ? Collections.emptyList() : Arrays.asList(children);

    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public RuleDescription firstChild() {
        return children.get(0);
    }

    public RuleDescription secondChild() {
        return children.get(1);
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public String getRuleIdentification() {
        return ruleIdentification;
    }

    public List<RuleDescription> getChildren() {
        return children;
    }

    public RuleDescription lastChild() {
        return children.get(children.size() - 1);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
