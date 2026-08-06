package no.nav.fpsak.nare.doc.vis;

import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.doc.RuleEdge;
import no.nav.fpsak.nare.doc.RuleNode;
import no.nav.fpsak.nare.doc.RuleNodeIdProducer;
import no.nav.fpsak.nare.evaluation.EvaluationRuleDescription;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;

/*
 * Section to process Rulespecifications or evaluations
 */
class RuleDescriptionProcessor {

    // Bruker her HashMap for å deduplisere tilfelle + skaffe erfaring. Obs på beregning som itererer over andeler.
    // Json-varianten i RuleDescriptionDigraph har en IdentityHashMap som vil spenne ut treet med mange kopier
    private final IdentityHashMap<RuleDescription, RuleNode> processed = new IdentityHashMap<>();
    private final RuleNodeIdProducer idProducer;
    private final List<RuleNode> nodes = new ArrayList<>();
    private final List<RuleEdge> edges = new ArrayList<>();


    RuleDescriptionProcessor() {
        this.idProducer = new IncrementalIdProcucer();
    }

    RuleDescriptionMermaidDigraph toMermaidDigraph(RuleDescription root) {
        process(root);
        return new RuleDescriptionMermaidDigraph(nodes, edges);
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
            edges.add(new RuleEdge(fromNode, childNode, "s" + ++i));
            // TODO vurder piler fra spec til hvert steg vs piler fra steg til steg: fromNode = childNode;
        }
    }

    private void processCompIf(RuleDescription ruledesc, RuleNode myNode) {
        var ifNode = process(ruledesc.firstChild());
        edges.add(new RuleEdge(myNode, ifNode, "test"));
        if (ifNode.rule() instanceof EvaluationRuleDescription eval) {
            if (ruledesc.getChildren().size() > 2) {
                throw new IllegalStateException("ComputationalIf has evaluated 2 conditional branches: " + ruledesc);
            } else if (ruledesc.getChildren().size() == 2) {
                var erole = Resultat.JA.equals(eval.getResultat()) ? "hvisja" : "hvisnei";
                var condNode = process(ruledesc.lastChild());
                edges.add(new RuleEdge(myNode, condNode, erole));
            }
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

    private static class IncrementalIdProcucer implements RuleNodeIdProducer {
        private int index = 0;

        @Override
        public String produceId() {
            index++;
            return "n" + index;
        }
    }

}
