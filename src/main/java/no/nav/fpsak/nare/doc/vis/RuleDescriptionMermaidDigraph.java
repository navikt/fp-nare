package no.nav.fpsak.nare.doc.vis;

import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.doc.RuleDescriptionDeserializedDigraph;
import no.nav.fpsak.nare.doc.RuleEdge;
import no.nav.fpsak.nare.doc.RuleNode;
import no.nav.fpsak.nare.doc.RuleNodeIdProducer;
import no.nav.fpsak.nare.evaluation.EvaluationRuleDescription;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.specification.SpecificationRuleDescription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RuleDescriptionMermaidDigraph {

    // Bruker her HashMap for å deduplisere tilfelle + skaffe erfaring. Obs på beregning som itererer over andeler.
    // Json-varianten i RuleDescriptionDigraph har en IdentityHashMap som vil spenne ut treet med mange kopier
    private final IdentityHashMap<RuleDescription, RuleNode> processed = new IdentityHashMap<>();
    private RuleNodeIdProducer idProducer;

    private final List<RuleNode> nodes = new ArrayList<>();
    private final List<RuleEdge> edges = new ArrayList<>();

    public RuleDescriptionMermaidDigraph(RuleDescription root, RuleNodeIdProducer idProducer) {
        this.idProducer = idProducer;
        process(root);
    }

    public RuleDescriptionMermaidDigraph(RuleDescriptionDeserializedDigraph digraph, boolean isSpecification) {
        var inputEdges = digraph.edges().stream().map(e -> new RuleEdge(e.source(), e.target(), e.role())).toList();
        var inputNodes = digraph.nodes().stream().map(n -> fromDeserializedRuleNode(n, isSpecification)).toList();
        var labeledEdges = labelDeserializedEdges(inputNodes.stream().collect(Collectors.toMap(RuleNode::id, n -> n)), inputEdges);
        nodes.addAll(inputNodes);
        edges.addAll(labeledEdges);
    }

    public Collection<RuleNode> getNodes() {
        return nodes;
    }

    public Collection<RuleEdge> getEdges() {
        return edges;
    }

    /*
     * Section to process deserialized evaluations or specifications, based on EvaluationSerializer
     */
    private static RuleNode fromDeserializedRuleNode(RuleDescriptionDeserializedDigraph.RuleNodeDeser node, boolean isSpecification) {
        if (isSpecification) {
            var ruleDescription = new SpecificationRuleDescription(node.operator(), node.ruleId(), node.ruleDescription());
            return new RuleNode(node.id(), node.ruleId(), node.ruleDescription(), node.operator(), ruleDescription);
        } else {
            var resultat = Optional.ofNullable(node.rule()).map(RuleDescriptionDeserializedDigraph.RuleDescriptionDeser::resultat).orElse(null);
            var reason = Optional.ofNullable(node.rule()).map(RuleDescriptionDeserializedDigraph.RuleDescriptionDeser::reason).orElse(null);
            var evaluationProperties = Optional.ofNullable(node.rule()).map(RuleDescriptionDeserializedDigraph.RuleDescriptionDeser::evaluationProperties).orElse(null);
            var ruleDescription = new EvaluationRuleDescription(node.operator(), node.ruleId(), node.ruleDescription(), resultat, reason, evaluationProperties);
            return new RuleNode(node.id(), node.ruleId(), node.ruleDescription(), node.operator(), ruleDescription);
        }
    }

    private List<RuleEdge> labelDeserializedEdges(Map<String, RuleNode> nodes, List<RuleEdge> inputEdges) {
        var edges = new ArrayList<>(inputEdges);
        for (var nodeId : nodes.keySet()) {
            var node = nodes.get(nodeId);
            var fromEdges = edges.stream().filter(e -> e.source().equals(node.id())).toList();
            if (fromEdges.stream().anyMatch(e -> e.role() != null && !e.role().isEmpty())) {
                continue;
            }
            if (node.operator() == Operator.SEQUENCE) {
                edges.removeAll(fromEdges);
                var fromNodeId = node.id();
                var children = fromEdges.stream().map(e -> nodes.get(e.target())).toList();
                var i = 1;
                for (var child: children) {
                    edges.add(new RuleEdge(fromNodeId, child.id(), node.id() + "." + i++));
                    // TODO: vurder steg->steg. Denne gir piler fra sekvens til steg: fromNodeId = child.id();
                }
            }
            if (node.operator() == Operator.COMPUTATIONAL_IF) {
                edges.removeAll(fromEdges);
                var children = fromEdges.stream().map(e -> nodes.get(e.target())).toList();
                var ifNode = children.getFirst();
                edges.add(new RuleEdge(node.id(), ifNode.id(), "test"));
                if (ifNode.rule() instanceof EvaluationRuleDescription eval) {
                    if (children.size() > 1) {
                        var erole = Resultat.JA.equals(eval.getResultat()) ? "hvisja" : "hvisnei";
                        edges.add(new RuleEdge(node.id(), children.getLast().id(), erole));
                    }
                } else {
                    edges.add(new RuleEdge(node.id(), children.get(1).id(), "hvisja"));
                    if (children.size() > 2) {
                        edges.add(new RuleEdge(node.id(), children.get(2).id(), "hvisnei"));
                    }
                }
            }
        }
        return edges;
    }

    /*
     * Section to process specifications or evaluations
     */
    private RuleNode process(RuleDescription ruledesc) {
        var prev = processed.get(ruledesc);
        if (prev != null) {
            return prev;
        }
        RuleNode myNode = new RuleNode(idProducer.produceId(), ruledesc);
        nodes.add(myNode);
        processed.put(ruledesc, myNode);

        if (ruledesc.getOperator() == Operator.COND_OR && erConditionalOrSpecification(ruledesc)) {
            processCondOrNodes(ruledesc, myNode);
        } else if (ruledesc.getOperator() == Operator.SEQUENCE) {
            processSequence(ruledesc, myNode);
        } else if (ruledesc.getOperator() == Operator.COMPUTATIONAL_IF) {
            processCompIf(ruledesc, myNode);
        } else {
            processRegularNodes(ruledesc, myNode);
        }
        return myNode;
    }

    private void processRegularNodes(RuleDescription ruledesc, RuleNode myNode) {
        for (RuleDescription child : ruledesc.getChildren()) {
            RuleNode childNode = process(child);
            edges.add(new RuleEdge(myNode, childNode, null));
        }
    }

    private void processSequence(RuleDescription ruledesc, RuleNode myNode) {
        var fromNode = myNode;
        int i = 0;
        for (RuleDescription child : ruledesc.getChildren()) {
            RuleNode childNode = process(child);
            // Teller og bruker sekvensens node-id som prefiks for å kunne spore stegene
            edges.add(new RuleEdge(fromNode, childNode, myNode.id() + "." + ++i));
            // TODO vurder piler fra spec til hvert steg vs piler fra steg til steg: fromNode = childNode;
        }
    }

    private void processCompIf(RuleDescription ruledesc, RuleNode myNode) {
        var ifNode = process(ruledesc.firstChild());
        edges.add(new RuleEdge(myNode, ifNode, "test"));
        if (ifNode.rule() instanceof EvaluationRuleDescription eval) {
            if (ruledesc.getChildren().size() > 2) {
                throw new IllegalStateException("ComputationalIf has evaluated 2 conditional branches: " + ruledesc);
            }
            var erole = Resultat.JA.equals(eval.getResultat()) ? "hvisja" : "hvisnei";
            var condNode = process(ruledesc.lastChild());
            edges.add(new RuleEdge(myNode, condNode, erole));
        } else {
            var thenNode = process(ruledesc.secondChild());
            edges.add(new RuleEdge(myNode, thenNode, "hvisja"));
            if (ruledesc.getChildren().size() > 2) {
                var elseNode = process(ruledesc.lastChild());
                edges.add(new RuleEdge(myNode, elseNode, "hvisnei"));
            }
        }

    }

    // Forksjell på EvalDescriptions og SpecDescriptions her
    // SpecDescription vil ha en magisk unær AND ..... og man lager en rolle på edge
    // EvalDescription kun vil treffe en av grenene
    private boolean erConditionalOrSpecification(RuleDescription description) {
        return description.getChildren().stream()
            .anyMatch(c -> Operator.AND.equals(c.getOperator()) && c.getChildren().size() == 1);
    }

    /*
     * TODO: Vurder et binært expression-tree, som dette i realiteten er
     */
    private void processCondOrNodes(RuleDescription ruledesc, RuleNode condorNode) {
        for (RuleDescription child : ruledesc.getChildren()) {
            // SpecDescription vil ha en magisk unær AND ..... og man lager en rolle på edge
            if (child.getOperator() == Operator.AND && child.getChildren().size() == 1) {
                RuleNode flowChild = process(child.firstChild());
                // TODO: Evaluere denne. OBS RuleDescriptionDigraph krever description og tar ikke med id (parent->condition)
                String edgeRole = Optional.ofNullable(flowChild.ruleId()).filter(s -> !s.isEmpty())
                        .orElseGet(flowChild::ruleDescription);
                edges.add(new RuleEdge(condorNode, flowChild, edgeRole));
            } else {
                RuleNode childNode = process(child);
                edges.add(new RuleEdge(condorNode, childNode, "ellers"));
            }
        }
    }


}
