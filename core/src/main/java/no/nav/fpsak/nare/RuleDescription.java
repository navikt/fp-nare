package no.nav.fpsak.nare;

import java.util.Arrays;
import java.util.List;

import com.google.gson.GsonBuilder;

import no.nav.fpsak.nare.evaluation.Operator;

public class RuleDescription {

    private String ruleIdentifcation;
    private String ruleDescription;
    private Operator operator;
    private List<RuleDescription> children;

    public RuleDescription(Operator operator, String ruleIdentifcation, String ruleDescription, RuleDescription... children) {
        this.operator = operator;
        this.children = Arrays.asList(children);
        this.ruleIdentifcation = ruleIdentifcation;
        this.ruleDescription = ruleDescription;

    }

    public RuleDescription(String ruleIdentifcation, String beskrivelse) {
        this.ruleDescription = beskrivelse;
        this.ruleIdentifcation = ruleIdentifcation;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
