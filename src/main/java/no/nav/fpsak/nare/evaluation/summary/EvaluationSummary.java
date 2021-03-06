package no.nav.fpsak.nare.evaluation.summary;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

/** Hjelpeklasse for å lage sammendrag av en evaluering. */
public class EvaluationSummary {

    private final class LeafNodeVisitorCondition implements EvaluationVisitorCondition {
        private final Set<Resultat> accepted;

        private LeafNodeVisitorCondition(Set<Resultat> accepted) {
            this.accepted = accepted;
        }

        @Override
        public boolean check(Operator op, Evaluation parent, Evaluation child) {
            if (!(Operator.SINGLE.equals(op) && accepted.contains(child.result()))) {
                return false;
            }

            // special case, kun prosessere siste SingleEvaluation barn av SequenceEvaluation siden alle andre
            // returnerer ja.
            if (Operator.SEQUENCE.equals(parent.getOperator())) {
                String childRuleId = child.ruleIdentification();
                String lastChildOfParentRuleId = ((AggregatedEvaluation) parent).lastChild().ruleIdentification();
                return (childRuleId.equals(lastChildOfParentRuleId));
            } else {
                return true;
            }
        }
    }

    interface EvaluationVisitorCondition {
        boolean check(Operator operator, Evaluation parent, Evaluation child);
    }

    private Evaluation rootEvaluation;

    public EvaluationSummary(Evaluation rootEvaluation) {
        this.rootEvaluation = rootEvaluation;
    }

    /** Convenience metode for å kun returnere et Leaf. Hvis det finnes flere kastes en exception. */
    public Optional<Evaluation> singleLeafEvaluation(Resultat... acceptedResults) {
        Collection<Evaluation> leafEvaluations = leafEvaluations(acceptedResults);

        if (leafEvaluations.size() > 1) {
            throw new IllegalStateException("Det finnes flere enn ett Leaf resultat for angitt aksepterte resultater (" //$NON-NLS-1$
                    + Arrays.toString(acceptedResults) + "): " + leafEvaluations); //$NON-NLS-1$
        } else if (leafEvaluations.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(leafEvaluations.iterator().next());
        }
    }
    
    /** Convenience metode for å kun returnere et Leaf. Hvis det finnes flere kastes en exception. */
    public Optional<String> singleLeafReason(Resultat... acceptedResults) {
        Collection<String> leafReasons = leafReasons(acceptedResults);

        if (leafReasons.size() > 1) {
            throw new IllegalStateException("Det finnes flere enn ett Leaf resultat for angitt aksepterte resultater (" //$NON-NLS-1$
                    + Arrays.toString(acceptedResults) + "): " + leafReasons); //$NON-NLS-1$
        } else if (leafReasons.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(leafReasons.iterator().next());
        }
    }

    public Collection<Evaluation> leafEvaluations(Resultat... acceptedResults) {
        Set<Resultat> accepted = acceptedResults.length > 0 ? EnumSet.copyOf(Arrays.asList(acceptedResults))
                : EnumSet.allOf(Resultat.class);

        NonCircularVisitorEvaluationCollector visitor = new NonCircularVisitorEvaluationCollector(new LeafNodeVisitorCondition(accepted));
        rootEvaluation.visit(rootEvaluation, visitor);
        return visitor.getCollected();
    }

    public Collection<String> leafReasons(Resultat... acceptedResults) {

        Set<Resultat> accepted = acceptedResults.length > 0 ? EnumSet.copyOf(Arrays.asList(acceptedResults))
                : EnumSet.allOf(Resultat.class);

        NonCircularVisitorEvaluationCollector visitor = new NonCircularVisitorEvaluationCollector(new LeafNodeVisitorCondition(accepted));

        rootEvaluation.visit(rootEvaluation, visitor);
        return visitor.getCollected().stream()
                .filter(e -> e.getOutcome() != null)
                .map(this::getReasonCode).distinct().collect(Collectors.toList());
    }

    private String getReasonCode(Evaluation e) {
        return e.getOutcome() == null ? null : e.getOutcome().getReasonCode();
    }
}
