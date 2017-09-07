package no.nav.fpsak.nare.doc;

import java.io.Writer;

import no.nav.fpsak.nare.RuleDescription;
import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.OrSpecification;
import no.nav.fpsak.nare.specification.Specification;

public class PlantUmlWriter {

    public PlantUmlWriter() {
    }

    public void write(RuleService<?> ruleService, Writer writer) {
        // FIXME (FC)
    }

    public <T> String writeToString(RuleService<T> ruleService) {
        Specification<T> spec = ruleService.getSpecification();

        StringBuffer buf = new StringBuffer();
        buf.append("@startuml");
        spec.visit(spec, (parentSpecification, childSpecification) -> {
            String line = "";
            if (childSpecification instanceof LeafSpecification) {
                line += ":" + childSpecification.identifikator();
            } else if (childSpecification instanceof OrSpecification) {
            }
            buf.append(line + "\n");
        });

        buf.append("@enduml");
        return buf.toString();
    }

    // TODO (FC) Flytt til egen klasse
    public String toPlantUmlActivityDiagram(RuleDescription ruleDescription) {
        return toPlantUmlText(ruleDescription, false);
    }

    protected String toPlantUmlText(RuleDescription rd, boolean alreadyNested) {
        StringBuffer buf = new StringBuffer();
        Operator operator = rd.getOperator();
        if (operator == null) {
            for (RuleDescription child : rd.getChildren()) {
                buf.append(toPlantUmlText(child, true));
            }
        } else {

            switch (operator) {
                case AND:
                    toAndSpecPlantUml(rd, alreadyNested, buf);
                    break;
                case OR:
                    toOrSpecPlantUml(rd, alreadyNested, buf);
                    break;
                case COND_OR:
                    toCondOrSpecPlantUml(rd, alreadyNested, buf);
                    break;
                case NOT:
                    toNotSpecPlantUml(rd, alreadyNested, buf);
                    break;
                case SINGLE:
                    toSinglePlantUml(rd, alreadyNested, buf);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported operator:" + operator);
            }
        }

        return buf.toString();
    }

    private void toOrSpecPlantUml(RuleDescription ruledesc, boolean alreadyNested, StringBuffer buf) {
        buf.append("if (" + label(ruledesc) + ") then \n");
        buf.append(toPlantUmlText(ruledesc.firstChild(), false)).append('\n');
        for (int i = 1; i < ruledesc.getChildren().size(); i++) {
            buf.append("else  \n");
            RuleDescription child = ruledesc.getChildren().get(i);
            buf.append(toPlantUmlText(child, false)).append('\n');
        }

        buf.append("endif \n");

    }

    private void toCondOrSpecPlantUml(RuleDescription ruledesc, boolean alreadyNested, StringBuffer buf) {
        buf.append("start \n");
        buf.append("if (" + label(ruledesc) + ") then \n");

        for (int i = 0; i < ruledesc.getChildren().size(); i++) {
            RuleDescription child_i = ruledesc.getChildren().get(i);
            buf.append((i == 0 ? "if" : "elseif") + " (" + label(child_i.firstChild()) + ") then (ja) \n");
            RuleDescription secondChild = child_i.secondChild();
            buf.append(toPlantUmlText(secondChild, true)).append('\n');
        }
        buf.append("else \n");
        buf.append(" stop \n");
        buf.append("endif \n");
        buf.append("else \n");
        buf.append(" stop \n");
        buf.append("endif \n");
        buf.append("stop \n");
    }

    private void toAndSpecPlantUml(RuleDescription ruledesc, boolean alreadyNested, StringBuffer buf) {
        if (!alreadyNested) {
            toSinglePlantUml(ruledesc, alreadyNested, buf);
        }
        for (int i = 0; i < ruledesc.getChildren().size(); i++) {
            buf.append("fork " + (i > 0 ? "again" : "") + " \n");
            RuleDescription child = ruledesc.getChildren().get(i);
            buf.append(toPlantUmlText(child, false)).append('\n');
        }
        buf.append("end fork \n");

    }

    @SuppressWarnings("unused")
    private void toNotSpecPlantUml(RuleDescription ruledesc, boolean alreadyNested, StringBuffer buf) {
        buf.append(":!" + label(ruledesc) + ";\n");
    }

    private static String label(RuleDescription ruledesc) {
        return ruledesc.getRuleIdentification() + ": \n"
                + (ruledesc.getRuleIdentification() != null && !ruledesc.getRuleIdentification().trim().isEmpty()
                        ? ruledesc.getRuleDescription()
                        : "");
    }

    @SuppressWarnings("unused")
    private void toSinglePlantUml(RuleDescription rd, boolean alreadyNested, StringBuffer buf) {
        buf.append(":" + label(rd) + "|\n");
    }

}
