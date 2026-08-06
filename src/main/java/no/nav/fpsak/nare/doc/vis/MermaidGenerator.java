package no.nav.fpsak.nare.doc.vis;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.doc.RuleDocumentation;
import no.nav.fpsak.nare.doc.RuleNode;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.EvaluationRuleDescription;
import no.nav.fpsak.nare.evaluation.node.SingleEvaluation;
import no.nav.fpsak.nare.json.JsonOutput;
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

    public enum EvalOutputOptions { ALL, BASIC, OUTCOME, PROPERTIES }

    private MermaidGenerator() {
    }

    public static String asMermaid(Evaluation evaluation) {
        return asMermaid(evaluation, EvalOutputOptions.ALL);
    }

    public static String asMermaid(Evaluation evaluation, EvalOutputOptions evalOutputOptions) {
        var ruledesc = evaluation.toRuleDescription();
        var digraph = new RuleDescriptionProcessor().toMermaidDigraph(ruledesc);
        return digraphAsMermaid(digraph, evalOutputOptions);
    }

    public static String asMermaid(Specification<?> specification) {
        var ruledesc = specification.ruleDescription();
        var digraph = new RuleDescriptionProcessor().toMermaidDigraph(ruledesc);
        return digraphAsMermaid(digraph, EvalOutputOptions.BASIC);
    }

    public static String asMermaid(RuleService<?> ruleService) {
        return getRuleServiceHeader(ruleService) + asMermaid(ruleService.getSpecification());
    }

    public static String evaluationAsMermaid(RuleDescriptionDeserializedDigraph digraph) {
        return evaluationAsMermaid(digraph, EvalOutputOptions.ALL);
    }

    public static String evaluationAsMermaid(RuleDescriptionDeserializedDigraph digraph, EvalOutputOptions evalOutputOptions) {
        var mdigraph = DeserializedDigraphProcessor.toMermaidDigraph(digraph, false);
        return digraphAsMermaid(mdigraph, evalOutputOptions);
    }

    public static String evaluationAsMermaid(String json, EvalOutputOptions evalOutputOptions) {
        var digraph = JsonOutput.fromJson(json, RuleDescriptionDeserializedDigraph.class);
        return evaluationAsMermaid(digraph, evalOutputOptions);
    }

    public static String evaluationAsMermaid(String json) {
        return evaluationAsMermaid(json, EvalOutputOptions.ALL);
    }

    public static String specificationAsMermaid(RuleDescriptionDeserializedDigraph digraph) {
        var mdigraph = DeserializedDigraphProcessor.toMermaidDigraph(digraph, true);
        return digraphAsMermaid(mdigraph, EvalOutputOptions.BASIC);
    }

    public static String specificationAsMermaid(String json) {
        var digraph = JsonOutput.fromJson(json, RuleDescriptionDeserializedDigraph.class);
        return specificationAsMermaid(digraph);
    }

    private static String digraphAsMermaid(RuleDescriptionMermaidDigraph digraph, EvalOutputOptions outputOptions) {
        var b = new StringBuilder("graph TD\n");
        digraph.nodes().forEach(n -> b.append(nodeToMermaid(n, outputOptions)));
        digraph.edges().forEach(e -> {
            b.append(e.source()).append(" -->");
            if (e.role() != null && !e.role().isEmpty()) {
                b.append("|").append(e.role().replaceAll("[()\"']", "")).append("|");
            }
            b.append(" ").append(e.target()).append("\n");
        });
        return b.toString();
    }

    private static String nodeToMermaid(RuleNode n, EvalOutputOptions outputOptions) {
        String b = n.id() + nodeTitleOpenSymbol(n) +
                logicalOperatorPrefix(n) +
                nodeRuleIdDescriptionText(n, outputOptions) +
                nodeTitleCloseSymbol(n) + "\n";
        return b.replaceAll("[\"']", "")
                .replace("COMP HVIS/SÅ", "HVIS/SÅ")
                .replace("COND HVIS/SÅ", "HVIS/SÅ");
    }

    private static String nodeRuleIdDescriptionText(RuleNode n, EvalOutputOptions outputOptions) {
        var ruleId = Optional.ofNullable(n.ruleId()).filter(s -> !s.isEmpty() && !s.startsWith("("));
        var description = Optional.ofNullable(n.ruleDescription()).filter(s -> !s.isEmpty());
        var nonDefaultDescription = description.filter(d -> !d.startsWith("("));
        var tekst = ruleId.map(s -> "ID:" + s + (nonDefaultDescription.map(d -> "\n" + d).orElse("")))
                .or(() -> nonDefaultDescription)
                .orElseGet(() -> logicalOperatorPrefix(n).isEmpty() ? description.orElseGet(() -> n.operator().name()) : "");
        if (n.rule() instanceof EvaluationRuleDescription eval) {
            tekst = tekst + ekstraEvaluationText(eval, tekst, outputOptions);
        }
        return tekst.replaceAll("[(){}\\[\\]\"']", "");
    }

    private static String ekstraEvaluationText(EvaluationRuleDescription eval, String tekst, EvalOutputOptions outputOptions) {
        var ekstratekst = Optional.of(tekst).filter(s -> !s.isEmpty()).map(_ -> "\n").orElse("") + "Resultat: " + eval.getResultat();
        if (eval.getEvaluationProperties() != null && !eval.getEvaluationProperties().isEmpty() && outputProperties(outputOptions)) {
            var properties = eval.getEvaluationProperties().entrySet().stream()
                    .filter(e -> e.getValue() != null && !e.getValue().toString().isEmpty())
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("");
            ekstratekst = ekstratekst + (properties.isEmpty() ? "" : "\n\nProperties: " + properties);
        }
        if (eval instanceof DeserializedDigraphProcessor.DeserSingleEvaluationRuleDescription single && single.getOutcomeReason() != null && outputOutcome(outputOptions)) {
            var outcomes = single.getOutcomeReason().entrySet().stream()
                    .filter(e -> e.getValue() != null && !e.getValue().toString().isEmpty())
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("");
            ekstratekst = ekstratekst + (outcomes.isEmpty() ? "" : "\n\nOutcome: " + outcomes);
        } else if (eval instanceof SingleEvaluation.SingleEvaluationRuleDescription single && single.getOutcomeReason() != null && outputOutcome(outputOptions)) {
            var outcome = single.getOutcomeReason().toString();
            ekstratekst = ekstratekst + (outcome.isEmpty() ? "" : "\n\nOutcome: " + outcome);
        }
        return ekstratekst;
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

    private static boolean outputProperties(EvalOutputOptions outputOptions) {
        return outputOptions == EvalOutputOptions.ALL || outputOptions == EvalOutputOptions.PROPERTIES;
    }

    private static boolean outputOutcome(EvalOutputOptions outputOptions) {
        return outputOptions == EvalOutputOptions.ALL || outputOptions == EvalOutputOptions.OUTCOME;
    }

    private static String getRuleServiceHeader(RuleService<?> ruleService) {
        var serviceName = Optional.ofNullable(ruleService.getClass().getAnnotation(RuleDocumentation.class))
                .map(RuleDocumentation::value)
                .filter(s -> !s.isEmpty())
                .map(s -> ", referanse " + s)
                .orElse("");
        return String.format("---%ntitle: Regelklasse %s%s%n---%n", ruleService.getClass().getSimpleName(), serviceName);
    }

}
