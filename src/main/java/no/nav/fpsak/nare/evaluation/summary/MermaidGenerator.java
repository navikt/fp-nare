package no.nav.fpsak.nare.evaluation.summary;

import no.nav.fpsak.nare.doc.RuleDescriptionMermaidDigraph;
import no.nav.fpsak.nare.doc.RuleNode;
import no.nav.fpsak.nare.doc.RuleNodeIdProducer;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.specification.Specification;

import java.util.Optional;

public class MermaidGenerator {


    private MermaidGenerator() {
    }

    public static String asMermaid(Evaluation evaluation) {
        var desc = evaluation.toRuleDescription();
        var digraph = new RuleDescriptionMermaidDigraph(desc, new IncrementalIdProcucer());
        return asMermaid(digraph);
    }

    public static String asMermaid(Specification<?> specification) {
        var digraph = new RuleDescriptionMermaidDigraph(specification.ruleDescription(), new IncrementalIdProcucer());
        return asMermaid(digraph);
    }

    private static String asMermaid(RuleDescriptionMermaidDigraph digraph) {
        var b = new StringBuilder("graph TD\n");
        digraph.getNodes().forEach(n -> b.append(nodeToMermaid(n)));
        digraph.getEdges().forEach(e -> {
            b.append(e.source()).append(" -->");
            if (e.role() != null && !e.role().isEmpty()) {
                b.append("|").append(e.role().replaceAll("[()]", "")).append("|");
            }
            b.append(" ").append(e.target()).append("\n");
        });
        return b.toString();
    }

    private static String nodeToMermaid(RuleNode n) {
        String b = n.id() + nodeTitleOpenSymbol(n) +
                nodeRuleIdText(n) + n.ruleDescription() +
                nodeTitleCloseSymbol(n) + "\n";
        return b.replaceAll("[()]", "");
    }

    private static String nodeRuleIdText(RuleNode n) {
        var hasDescription = Optional.ofNullable(n.ruleDescription()).filter(s -> !s.isEmpty()).isPresent();
        return Optional.ofNullable(n.ruleId())
                .filter(s -> !s.isEmpty() && !s.startsWith("("))
                .map(s -> "ID:" + s + (hasDescription ? "\n" : ""))
                .orElse("");
    }

    private static String nodeTitleOpenSymbol(RuleNode n) {
        return Operator.COMPUTATIONAL_IF == n.operator() ? "{" : "[";
    }

    private static String nodeTitleCloseSymbol(RuleNode n) {
        return Operator.COMPUTATIONAL_IF == n.operator() ? "}" : "]";
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
