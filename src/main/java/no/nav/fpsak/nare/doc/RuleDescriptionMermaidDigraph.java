package no.nav.fpsak.nare.doc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.json.JsonOutput;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RuleDescriptionMermaidDigraph {

    // Bruker her HashMap for å deduplisere tilfelle + skaffe erfaring. Obs på beregning som itererer over andeler.
    // Json-varianten i RuleDescriptionDigraph har en IdentityHashMap som vil spenne ut treet med mange kopier
    private final LinkedHashMap<RuleDescription, RuleNode> processed = new LinkedHashMap<>();
    private final RuleNodeIdProducer idProducer;

    private final LinkedHashSet<RuleNode> nodes = new LinkedHashSet<>();
    private final LinkedHashSet<RuleEdge> edges = new LinkedHashSet<>();

    public RuleDescriptionMermaidDigraph(RuleDescription root, RuleNodeIdProducer idProducer) {
        this.idProducer = idProducer;
        process(root);
    }

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

    protected void processRegularNodes(RuleDescription ruledesc, RuleNode myNode) {
        for (RuleDescription child : ruledesc.getChildren()) {
            RuleNode childNode = process(child);
            edges.add(new RuleEdge(myNode, childNode, null));
        }
    }

    protected void processSequence(RuleDescription ruledesc, RuleNode myNode) {
        var fromNode = myNode;
        int i = 0;
        for (RuleDescription child : ruledesc.getChildren()) {
            RuleNode childNode = process(child);
            // Teller og bruker sekvensens node-id som prefiks for å kunne spore stegene
            edges.add(new RuleEdge(fromNode, childNode, myNode.id() + "." + ++i));
            fromNode = childNode;
        }
    }

    protected void processCompIf(RuleDescription ruledesc, RuleNode myNode) {
        var ifNode = process(ruledesc.firstChild());
        var thenNode = process(ruledesc.secondChild());
        edges.add(new RuleEdge(myNode, ifNode, "test"));
        edges.add(new RuleEdge(myNode, thenNode, "hvisja"));
        if (ruledesc.getChildren().size() > 2) {
            var elseNode = process(ruledesc.lastChild());
            edges.add(new RuleEdge(myNode, elseNode, "hvisnei"));
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
    protected void processCondOrNodes(RuleDescription ruledesc, RuleNode condorNode) {
        for (RuleDescription child : ruledesc.getChildren()) {
            // SpecDescription vil ha en magisk unær AND ..... og man lager en rolle på edge
            if (child.getOperator() == Operator.AND && child.getChildren().size() == 1) {
                RuleNode flowChild = process(child.firstChild());
                String edgeRole = Optional.ofNullable(flowChild.ruleId()).filter(s -> !s.isEmpty())
                        .orElseGet(flowChild::ruleDescription); // TODO: Evaluere denne
                edges.add(new RuleEdge(condorNode, flowChild, edgeRole));
            } else {
                RuleNode childNode = process(child);
                edges.add(new RuleEdge(condorNode, childNode, "ellers"));
            }
        }
    }

    public Set<RuleNode> getNodes() {
        return nodes;
    }

    public Set<RuleEdge> getEdges() {
        return edges;
    }
}
