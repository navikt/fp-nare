package no.nav.fpsak.nare.doc.vis;

import no.nav.fpsak.nare.doc.RuleEdge;
import no.nav.fpsak.nare.doc.RuleNode;
import no.nav.fpsak.nare.evaluation.EvaluationRuleDescription;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.specification.SpecificationRuleDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Processor for deserialized evaluations or specifications, based on EvaluationSerializer.
 * Populerer ikke RuleNode/rule/children i denne omgangen
 */
class DeserializedDigraphProcessor {

    private DeserializedDigraphProcessor() {
    }

    static RuleDescriptionMermaidDigraph toMermaidDigraph(RuleDescriptionDeserializedDigraph digraph, boolean isSpecification) {
        var inputEdges = digraph.edges().stream().map(e -> new RuleEdge(e.source(), e.target(), e.role())).toList();
        var inputNodes = digraph.nodes().stream().map(n -> fromDeserializedRuleNode(n, isSpecification)).toList();
        var labeledEdges = labelDeserializedEdges(inputNodes.stream().collect(Collectors.toMap(RuleNode::id, n -> n)), inputEdges);
        return new RuleDescriptionMermaidDigraph(inputNodes, labeledEdges);
    }

    private static RuleNode fromDeserializedRuleNode(RuleDescriptionDeserializedDigraph.RuleNodeDeser node, boolean isSpecification) {
        if (isSpecification) {
            var ruleDescription = new SpecificationRuleDescription(node.operator(), node.ruleId(), node.ruleDescription());
            return new RuleNode(node.id(), node.ruleId(), node.ruleDescription(), node.operator(), ruleDescription);
        } else {
            var resultat = Optional.ofNullable(node.rule()).map(RuleDescriptionDeserializedDigraph.RuleDescriptionDeser::resultat).orElse(null);
            var reason = Optional.ofNullable(node.rule()).map(RuleDescriptionDeserializedDigraph.RuleDescriptionDeser::reason).orElse(null);
            var evaluationProperties = Optional.ofNullable(node.rule()).map(RuleDescriptionDeserializedDigraph.RuleDescriptionDeser::evaluationProperties).orElse(null);
            var outcome = Optional.ofNullable(node.rule()).map(RuleDescriptionDeserializedDigraph.RuleDescriptionDeser::outcomeReason).orElse(null);
            var ruleDescription = outcome == null || outcome.isEmpty()
                    ? new EvaluationRuleDescription(node.operator(), node.ruleId(), node.ruleDescription(), resultat, reason, evaluationProperties)
                    : new DeserSingleEvaluationRuleDescription(node.operator(), node.ruleId(), node.ruleDescription(), resultat, reason, evaluationProperties, outcome);
            return new RuleNode(node.id(), node.ruleId(), node.ruleDescription(), node.operator(), ruleDescription);
        }
    }

    private static List<RuleEdge> labelDeserializedEdges(Map<String, RuleNode> nodes, List<RuleEdge> inputEdges) {
        var resultedges = new ArrayList<>(inputEdges);
        for (var nodeEntry : nodes.entrySet()) {
            var node = nodeEntry.getValue();
            var fromEdges = resultedges.stream().filter(e -> e.source().equals(node.id())).toList();
            if (fromEdges.stream().anyMatch(e -> e.role() != null && !e.role().isEmpty())) {
                continue;
            }
            if (node.operator() == Operator.SEQUENCE) {
                resultedges.removeAll(fromEdges);
                resultedges.addAll(labelSequenceEdges(nodes, node, fromEdges));
            } else if (node.operator() == Operator.COMPUTATIONAL_IF) {
                resultedges.removeAll(fromEdges);
                resultedges.addAll(labelComputationalIfEdges(nodes, node, fromEdges));
            }
        }
        return resultedges;
    }

    private static List<RuleEdge> labelSequenceEdges(Map<String, RuleNode> nodes, RuleNode node, List<RuleEdge> edges) {
        var resultedges = new ArrayList<RuleEdge>();
        var fromNodeId = node.id();
        var children = edges.stream().map(e -> nodes.get(e.target())).toList();
        var i = 1;
        for (var child: children) {
            resultedges.add(new RuleEdge(fromNodeId, child.id(), "seq." + i++));
            // TODO: vurder steg->steg. Denne gir piler fra sekvens til steg: fromNodeId = child.id();
        }
        return resultedges;
    }

    private static List<RuleEdge> labelComputationalIfEdges(Map<String, RuleNode> nodes, RuleNode node, List<RuleEdge> edges) {
        var resultedges = new ArrayList<RuleEdge>();
        var children = edges.stream().map(e -> nodes.get(e.target())).toList();
        var ifNode = children.getFirst();
        resultedges.add(new RuleEdge(node.id(), ifNode.id(), "test"));
        if (ifNode.rule() instanceof EvaluationRuleDescription eval) {
            if (children.size() > 1) {
                var erole = Resultat.JA.equals(eval.getResultat()) ? "hvisja" : "hvisnei";
                resultedges.add(new RuleEdge(node.id(), children.getLast().id(), erole));
            }
        } else {
            resultedges.add(new RuleEdge(node.id(), children.get(1).id(), "hvisja"));
            if (children.size() > 2) {
                resultedges.add(new RuleEdge(node.id(), children.get(2).id(), "hvisnei"));
            }
        }
        return resultedges;
    }

    static class DeserSingleEvaluationRuleDescription extends EvaluationRuleDescription {
        private final Map<String, Object> outcomeReason;

        public DeserSingleEvaluationRuleDescription(Operator operator, String ruleId, String ruleDescription, Resultat resultat, String reason,
                                                    Map<String, Object> evaluationProperties, Map<String, Object> outcomeReason) {
            super(operator, ruleId, ruleDescription, resultat, reason, evaluationProperties);
            this.outcomeReason = Optional.ofNullable(outcomeReason).orElse(Map.of()).entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        public Map<String, Object> getOutcomeReason() {
            return outcomeReason;
        }
    }



}
