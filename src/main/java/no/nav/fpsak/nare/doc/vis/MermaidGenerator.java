package no.nav.fpsak.nare.doc.vis;

import no.nav.fpsak.nare.doc.RuleDescriptionDeserializedDigraph;
import no.nav.fpsak.nare.doc.RuleNode;
import no.nav.fpsak.nare.doc.RuleNodeIdProducer;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.EvaluationRuleDescription;
import no.nav.fpsak.nare.specification.Specification;

import java.util.Optional;

/*
 * Enkel generator som kan lage grafer fra evalueringer eller spesifikasjoner, direkte eller fra en lagret/serialisert versjon
 * Tar med resultat JA/NEI for evalueringer. Har ikke med reasons eller evaluationProperties i denne omgangen.
 *
 * Bruker 4 enkle symboler: sirkel for and/or/not, diamant for if, firkant for enkel og ||sekvens||. Bruker ikke subgrafer (ennå)
 *
 * Kommentarer
 * - Må gjennomgå bruk av ruleId/ruleDescription vs RuleSet for å få gode kilder, fx condOr i RuleDescriptionDigraph (ikke ID)
 * - Sekvens peke rett ned på alle understeg, kan endre til å kjede stegene - må da tenke på om siste skal tilbake til sekvensen
 * - CompIf er som regel uten id/description pga RuleSet - pull up test til diamanten
 * - CondOr bør studeres
 * - RuleSet: ID-løse sekvenser? ID/Desc for CompIf? Trengs CompIf uten else? AND/OR/NOT - trenger disse ID/navn?
 *
 * Ellers bør man se på om RuleService bør styrkes og få ID/Description - må da se på hvordan traversere Specifications.
 */
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

    public static String evaluationAsMermaid(RuleDescriptionDeserializedDigraph digraph) {
        var mdigraph = new RuleDescriptionMermaidDigraph(digraph, false);
        return asMermaid(mdigraph);
    }

    public static String specificationAsMermaid(RuleDescriptionDeserializedDigraph digraph) {
        var mdigraph = new RuleDescriptionMermaidDigraph(digraph, true);
        return asMermaid(mdigraph);
    }

    private static String asMermaid(RuleDescriptionMermaidDigraph digraph) {
        var b = new StringBuilder("graph TD\n");
        digraph.getNodes().forEach(n -> b.append(nodeToMermaid(n)));
        digraph.getEdges().forEach(e -> {
            b.append(e.source()).append(" -->");
            if (e.role() != null && !e.role().isEmpty()) {
                b.append("|").append(e.role().replaceAll("[()\"']", "")).append("|");
            }
            b.append(" ").append(e.target()).append("\n");
        });
        return b.toString();
    }

    private static String nodeToMermaid(RuleNode n) {
        String b = n.id() + nodeTitleOpenSymbol(n) +
                logicalOperatorPrefix(n) +
                nodeRuleIdDescriptionText(n) +
                nodeTitleCloseSymbol(n) + "\n";
        return b.replaceAll("[\"']", "")
                .replace("COMP HVIS/SÅ", "HVIS/SÅ")
                .replace("COND HVIS/SÅ", "HVIS/SÅ");
    }

    private static String nodeRuleIdDescriptionText(RuleNode n) {
        var ruleId = Optional.ofNullable(n.ruleId()).filter(s -> !s.isEmpty() && !s.startsWith("("));
        var description = Optional.ofNullable(n.ruleDescription()).filter(s -> !s.isEmpty());
        var nonDefaultDescription = description.filter(d -> !d.startsWith("("));
        var tekst = ruleId.map(s -> "ID:" + s + (nonDefaultDescription.map(d -> "\n" + d).orElse("")))
                .or(() -> nonDefaultDescription)
                .orElseGet(() -> logicalOperatorPrefix(n).isEmpty() ? description.orElseGet(() -> n.operator().name()) : "");
        if (n.rule() instanceof EvaluationRuleDescription eval && eval.getResultat() != null) {
            tekst = Optional.of(tekst).filter(s -> !s.isEmpty()).map(s -> s + "\n").orElse("") + "Resultat: " + eval.getResultat();
        }
        return tekst.replaceAll("[()\"']", "");
    }

    private static String nodeTitleOpenSymbol(RuleNode n) {
        return switch (n.operator()) {
            case COMPUTATIONAL_IF, COND_OR -> "{" ;
            case SEQUENCE -> "[[" ;
            case AND, OR, NOT -> "((";
            case SINGLE -> "[";
        };
    }

    private static String nodeTitleCloseSymbol(RuleNode n) {
        return switch (n.operator()) {
            case COMPUTATIONAL_IF, COND_OR -> "}" ;
            case SEQUENCE -> "]]" ;
            case AND, OR, NOT -> "))";
            case SINGLE -> "]";
        };
    }

    private static String logicalOperatorPrefix(RuleNode n) {
        return switch (n.operator()) {
            case AND -> "OG\n";
            case OR ->  "ELLER\n";
            case NOT -> "IKKE\n";
            case SEQUENCE, SINGLE, COMPUTATIONAL_IF, COND_OR -> "";
        };
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
