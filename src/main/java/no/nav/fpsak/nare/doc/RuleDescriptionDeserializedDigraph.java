package no.nav.fpsak.nare.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

import java.util.List;
import java.util.Map;

/*
 * Needed due to ambiguous implementations of RuleDescription
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RuleDescriptionDeserializedDigraph(Map<String, String> versions, RuleNodeDeser root,
                                                 List<RuleNodeDeser> nodes, List<RuleEdgeDeser> edges) {


    public record RuleEdgeDeser(String source, String target, String role) { }

    public record RuleNodeDeser(String id, String ruleId, String ruleDescription, Operator operator, RuleDescriptionDeser rule) {}

    public record RuleDescriptionDeser(Resultat resultat, String reason, Map<String, Object> evaluationProperties) { }

}
