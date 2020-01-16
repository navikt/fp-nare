package no.nav.fpsak.nare.doc;

import java.util.IdentityHashMap;
import java.util.LinkedHashSet;

import no.nav.fpsak.nare.evaluation.Operator;

public class RuleDescriptionDigraph {

    private IdentityHashMap<RuleDescription, Boolean> processed = new IdentityHashMap<>();

    @SuppressWarnings("unused")
    private RuleNode root;

    private LinkedHashSet<RuleNode> nodes = new LinkedHashSet<>();

    private LinkedHashSet<RuleEdge> edges = new LinkedHashSet<>();

    public RuleDescriptionDigraph(RuleDescription root) {
        this.root = process(root);
    }

    private RuleNode process(RuleDescription ruledesc) {
        Boolean prev = processed.putIfAbsent(ruledesc, true);
        if (prev != null) {
            return null;
        }
        RuleNode myNode = new RuleNode(ruledesc);
        nodes.add(myNode);

        if (ruledesc.getOperator() == Operator.COND_OR) {
            processCondOrNodes(ruledesc, myNode);
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

    protected void processCondOrNodes(RuleDescription ruledesc, RuleNode condorNode) {
        for (RuleDescription child : ruledesc.getChildren()) {
            if (child.getOperator() == Operator.AND) {
                RuleNode flowChild = process(child.firstChild());
                String edgeRole = child.getRuleDescription();
                edges.add(new RuleEdge(condorNode, flowChild, edgeRole));
            } else {
                RuleNode childNode = process(child);
                edges.add(new RuleEdge(condorNode, childNode, "ellers"));
            }
        }
    }

    public String toJson() {
        return JsonOutput.asJson(this);
    }
}
