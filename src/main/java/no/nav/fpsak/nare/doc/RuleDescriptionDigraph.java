package no.nav.fpsak.nare.doc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.json.JsonOutput;

import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleDescriptionDigraph {

    @JsonIgnore
    private transient IdentityHashMap<RuleDescription, Boolean> processed = new IdentityHashMap<>();
    @JsonIgnore
    private transient RuleNodeIdProducer idProducer;

    @SuppressWarnings("unused")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "versions")
    private Map<String, String> versions;

    @SuppressWarnings("unused")
    @JsonProperty(value="root")
    private RuleNode root;

    @JsonProperty(value="nodes")
    private LinkedHashSet<RuleNode> nodes = new LinkedHashSet<>();

    @JsonProperty(value="edges")
    private LinkedHashSet<RuleEdge> edges = new LinkedHashSet<>();

    public RuleDescriptionDigraph(RuleDescription root, RuleNodeIdProducer idProducer, Map<String, String> versions) {
        this.idProducer = idProducer;
        this.root = process(root);
        this.versions = versions;
    }

    private RuleNode process(RuleDescription ruledesc) {
        Boolean prev = processed.putIfAbsent(ruledesc, true);
        if (prev != null) {
            return null;
        }
        RuleNode myNode = new RuleNode(idProducer.produceId(), ruledesc);
        nodes.add(myNode);

        if (ruledesc.getOperator() == Operator.COND_OR && erConditionalOrSpecification(ruledesc)) {
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
