package no.nav.fpsak.nare.doc;

import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.UUID;

import com.google.gson.GsonBuilder;

import no.nav.fpsak.nare.evaluation.Operator;

public class RuleDescriptionDigraph {

    private transient IdentityHashMap<RuleDescription, Boolean> processed = new IdentityHashMap<>();

    private LinkedHashSet<RuleNode> nodes = new LinkedHashSet<>();

    private LinkedHashSet<RuleEdge> edges = new LinkedHashSet<>();

    public RuleDescriptionDigraph(RuleDescription root) {

        process(root);
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
                RuleNode testChild = process(child.firstChild());
                RuleNode flowChild = process(child.secondChild());
                String edgeRole = testChild.nodeId;
                edges.add(new RuleEdge(condorNode, flowChild, edgeRole));
            } else {
                RuleNode childNode = process(child);
                edges.add(new RuleEdge(condorNode, childNode, "else"));
            }
        }
    }

    @SuppressWarnings("unused")
    public static class RuleNode {
        private final String nodeId = UUID.randomUUID().toString();

        private final String ruleId;
        private final String ruleDescription;
        private final Operator operator;

        public RuleNode(String ruleId, String ruleDescription, Operator operator) {
            super();
            this.ruleId = ruleId;
            this.ruleDescription = ruleDescription;
            this.operator = operator;
        }

        public RuleNode(RuleDescription node) {
            this.ruleId = node.getRuleIdentification();
            this.ruleDescription = node.getRuleDescription();
            this.operator = node.getOperator();
        }

    }

    public static class RuleEdge {
        String sourceNodeId;
        String targetNodeId;
        String role;

        public RuleEdge(RuleNode source, RuleNode target, String edgeRole) {
            this.sourceNodeId = source.nodeId;
            this.targetNodeId = target.nodeId;
            role = edgeRole;
        }
    }

    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
