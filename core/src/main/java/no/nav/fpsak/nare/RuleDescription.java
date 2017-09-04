package no.nav.fpsak.nare;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.gson.GsonBuilder;

import no.nav.fpsak.nare.evaluation.Operator;

public class RuleDescription {

    private final String ruleIdentifcation;
    private final String ruleDescription;
    private final Operator operator;
    private final List<RuleDescription> children;

    public RuleDescription(Operator operator, String ruleIdentifcation, String ruleDescription, RuleDescription... children) {
        Objects.requireNonNull(operator, "operator");
        Objects.requireNonNull(ruleIdentifcation, "ruleIdentification");
        Objects.requireNonNull(ruleDescription, "ruleDescription");

        if (Objects.equals(Operator.SINGLE, operator) && children != null && children.length > 0) {
            throw new IllegalArgumentException("Cannot have a SINGLE node with children : " + children.length);
        }
        this.operator = operator;
        this.ruleIdentifcation = ruleIdentifcation;
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

    // TODO (FC) Flytt til egen klasse
    public String toPlantUmlActivityDiagram() {
        return toPlantUmlText(false);
    }

    protected String toPlantUmlText(boolean alreadyNested) {
        StringBuffer buf = new StringBuffer();

        switch (operator) {
            case AND:
                toAndSpecPlantUml(alreadyNested, buf);
                break;
            case OR:
                toOrSpecPlantUml(alreadyNested, buf);
                break;
            case COND_OR:
                toCondOrSpecPlantUml(alreadyNested, buf);
                break;
            case NOT:
                toNotSpecPlantUml(alreadyNested, buf);
                break;
            case SINGLE:
                toSinglePlantUml(alreadyNested, buf);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operator:" + operator);
        }

        return buf.toString();
    }

    private void toOrSpecPlantUml(boolean alreadyNested, StringBuffer buf) {
        buf.append("if (" + label(this) + ") then \n");
        {
            buf.append(children.get(0).toPlantUmlText(true)).append('\n');
            for (int i = 1; i < children.size(); i++) {
                buf.append("else  \n");
                buf.append(children.get(i).toPlantUmlText(true)).append('\n');
            }
        }

        buf.append("endif \n");

    }

    private void toCondOrSpecPlantUml(boolean alreadyNested, StringBuffer buf) {
        buf.append("start \n");
        if (!alreadyNested) {
            buf.append("if (" + label(this) + ") then \n");
        }
        {
            RuleDescription child_0 = children.get(0);
            buf.append("if (" + label(child_0) + ") then (ja) \n");
            buf.append(child_0.toPlantUmlText(true)).append('\n');
            for (int i = 1; i < children.size(); i++) {
                RuleDescription child_i = children.get(i);
                buf.append("elseif (" + label(child_i) + ") then (ja) \n");
                buf.append(child_i.toPlantUmlText(true)).append('\n');
            }
            buf.append("else \n");
            buf.append(" stop \n");
            buf.append("endif \n");
        }

        if (!alreadyNested) {
            buf.append("endif \n");
        }
        buf.append("stop \n");

    }

    private void toAndSpecPlantUml(boolean alreadyNested, StringBuffer buf) {

            buf.append("if (" + label(this) + ") then \n");
        {
            for (int i = 0; i < children.size(); i++) {
                buf.append("fork " + (i > 0 ? "again" : "") + " \n");
                buf.append(children.get(i).toPlantUmlText(true)).append('\n');
            }
            buf.append("end fork \n");
        }
            buf.append("else \n");
            buf.append(" stop \n");

            buf.append("endif \n");

    }

    private void toNotSpecPlantUml(boolean alreadyNested, StringBuffer buf) {
        buf.append(":!" + label(this) + ";\n");
    }

    private static String label(RuleDescription ruledesc) {
        return ruledesc.ruleDescription != null && !ruledesc.ruleDescription.trim().isEmpty() ? ruledesc.ruleDescription
                : ruledesc.ruleIdentifcation;
    }

    private void toSinglePlantUml(boolean alreadyNested, StringBuffer buf) {
        buf.append(":" + label(this) + "|\n");
    }
}
