package no.nav.fpsak.nare.evaluation.summary;

import no.nav.fpsak.nare.doc.JsonOutput;
import no.nav.fpsak.nare.doc.RuleDescriptionDigraph;
import no.nav.fpsak.nare.evaluation.Evaluation;

public class EvaluationSerializer {

    /**
     * @deprecated Kun av historisk interesse. Bruk #asJson
     */
    @Deprecated
    public static String asLegacyJsonTree(Evaluation evaluation) {
        return JsonOutput.asJson(evaluation);
    }
    
    public static String asJson(Evaluation evaluation) {
        RuleDescriptionDigraph digraph = new RuleDescriptionDigraph(evaluation.toRuleDescription());
        return digraph.toJson();
    }

}
