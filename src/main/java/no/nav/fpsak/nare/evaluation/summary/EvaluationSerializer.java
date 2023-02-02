package no.nav.fpsak.nare.evaluation.summary;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import no.nav.fpsak.nare.doc.RuleDescriptionDigraph;
import no.nav.fpsak.nare.doc.RuleNodeIdProducer;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.Specification;

public class EvaluationSerializer {


    public static String asJson(Evaluation evaluation) {
        var desc = evaluation.toRuleDescription();
        RuleDescriptionDigraph digraph = new RuleDescriptionDigraph(desc, new EvaluationSerializer.IncrementalIdProcucer(), Map.of());
        return digraph.toJson();
    }

    public static String asJson(Evaluation evaluation, EvaluationVersion... versions) {
        var desc = evaluation.toRuleDescription();
        RuleDescriptionDigraph digraph = new RuleDescriptionDigraph(desc, new EvaluationSerializer.IncrementalIdProcucer(), toVersionsMap(versions));
        return digraph.toJson();
    }

    public static String asJson(Specification<?> specification, EvaluationVersion... versions) {
        RuleDescriptionDigraph digraph = new RuleDescriptionDigraph(specification.ruleDescription(), new EvaluationSerializer.IncrementalIdProcucer(), toVersionsMap(versions));
        return digraph.toJson();
    }

    private static Map<String, String> toVersionsMap(EvaluationVersion[] versions) {
        Map<String, String> map = Arrays.stream(versions).collect(Collectors.toMap(EvaluationVersion::name, EvaluationVersion::version));
        //hvis ingen versjoner er oppgitt, sender inn null for å unngå å få med versjon-elementet i sin helhet
        return map.isEmpty() ? null : map;
    }

    private static class IncrementalIdProcucer implements RuleNodeIdProducer {
        private int index = 0;

        @Override
        public String produceId() {
            index++;
            return "n" + index;
        }
    }

}
