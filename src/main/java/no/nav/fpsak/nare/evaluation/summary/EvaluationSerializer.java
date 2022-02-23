package no.nav.fpsak.nare.evaluation.summary;

import no.nav.fpsak.nare.doc.RuleDescriptionDigraph;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.Specification;

public class EvaluationSerializer {

    public static String asJson(Evaluation evaluation) {
        var desc = evaluation.toRuleDescription();
        RuleDescriptionDigraph digraph = new RuleDescriptionDigraph(desc);
        return digraph.toJson();
    }

    public static String asJson(Specification<?> specification) {
        RuleDescriptionDigraph digraph = new RuleDescriptionDigraph(specification.ruleDescription());
        return digraph.toJson();
    }

}
