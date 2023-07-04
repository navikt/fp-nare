package no.nav.fpsak.nare.doc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.json.JsonOutput;

@JsonInclude(Include.NON_NULL)
public class BasicRuleDescription implements RuleDescription {

    @JsonIgnore
    private final String ruleIdentification;
    @JsonIgnore
    private final String ruleDescription;
    @JsonIgnore
    private final Operator operator;
    @JsonIgnore
    private final List<RuleDescription> children;

    public BasicRuleDescription(Operator operator, String ruleIdentification, String ruleDescription,
            RuleDescription... children) {
        this(operator, ruleIdentification, ruleDescription,
                children == null || children.length == 0 ? Collections.emptyList() : Arrays.asList(children));
    }

    public BasicRuleDescription(Operator operator, String ruleIdentification, String ruleDescription,
            List<RuleDescription> children) {
        Objects.requireNonNull(ruleIdentification, "ruleIdentification");
        Objects.requireNonNull(ruleDescription, "ruleDescription");

        if (Objects.equals(Operator.SINGLE, operator) && children != null && !children.isEmpty()) {
            throw new IllegalArgumentException("Cannot have a SINGLE node with children : " + children.size());
        }
        this.operator = operator;
        this.ruleIdentification = ruleIdentification;
        this.ruleDescription = ruleDescription;
        this.children = children == null || children.isEmpty() ? Collections.emptyList() : children;

    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return JsonOutput.asJson(this);
    }

    @Override
    public RuleDescription firstChild() {
        return children.get(0);
    }

    @Override
    public RuleDescription secondChild() {
        return children.get(1);
    }

    @Override
    public String getRuleDescription() {
        return ruleDescription;
    }

    @Override
    public String getRuleIdentification() {
        return ruleIdentification;
    }

    @Override
    public List<RuleDescription> getChildren() {
        return children;
    }

    @Override
    public RuleDescription lastChild() {
        return children.get(children.size() - 1);
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
