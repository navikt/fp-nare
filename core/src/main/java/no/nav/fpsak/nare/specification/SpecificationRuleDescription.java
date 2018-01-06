package no.nav.fpsak.nare.specification;

import java.util.List;

import no.nav.fpsak.nare.doc.BasicRuleDescription;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Operator;

public class SpecificationRuleDescription extends BasicRuleDescription {

    public SpecificationRuleDescription(Operator operator, String ruleIdentification, String ruleDescription,
            List<RuleDescription> children) {
        super(operator, ruleIdentification, ruleDescription, children);
    }

    public SpecificationRuleDescription(Operator operator, String ruleIdentification, String ruleDescription, RuleDescription... children) {
        super(operator, ruleIdentification, ruleDescription, children);
    }
}
