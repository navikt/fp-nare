package no.nav.fpsak.nare.evaluation.summary;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.RuleReasonRef;

/** Hjelpeklasse for å lage sammendrag av en evaluering. */
public class EvaluationSummary {

    private final class LeafNodeVisitorCondition implements EvaluationVisitorCondition {
        private final Set<Resultat> accepted;
        private boolean all;

        private LeafNodeVisitorCondition(Set<Resultat> accepted, boolean all) {
            this.accepted = accepted;
            this.all = all;
        }

        @Override
        public boolean check(Operator op, Evaluation parent, Evaluation child) {
            if (!(Operator.SINGLE.equals(op) && accepted.contains(child.result()))) {
                return false;
            }
            if (all) {
                return true;
            }

            // special case, kun prosessere siste SingleEvaluation barn av SequenceEvaluation siden alle andre returnerer ja.
            if (Operator.SEQUENCE.equals(parent.getOperator()) || Operator.FOREACH.equals(parent.getOperator())) {
                String childRuleId = child.ruleIdentification();
                String lastChildOfParentRuleId = ((AggregatedEvaluation) parent).lastChild().ruleIdentification();
                return (childRuleId.equals(lastChildOfParentRuleId));
            } else {
                return true;
            }
        }
    }

    public interface EvaluationVisitorCondition {
        boolean check(Operator operator, Evaluation parent, Evaluation child);
    }

    private Evaluation rootEvaluation;

    public EvaluationSummary(Evaluation rootEvaluation) {
        this.rootEvaluation = rootEvaluation;
    }

    /**
     * Convenience metode for å kun returnere et Leaf. Hvis det finnes flere kastes en exception.
     */
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

    public Collection<Evaluation> leafEvaluations(Resultat... acceptedResults) {
        Set<Resultat> accepted = acceptedResults.length > 0 ? EnumSet.copyOf(Arrays.asList(acceptedResults))
                : EnumSet.allOf(Resultat.class);

        NonCircularVisitorEvaluationCollector visitor = new NonCircularVisitorEvaluationCollector(new LeafNodeVisitorCondition(accepted, false));
        rootEvaluation.visit(rootEvaluation, visitor);
        return visitor.getCollected();
    }

    public Collection<Evaluation> allLeafEvaluations(Resultat... acceptedResults) {
        Set<Resultat> accepted = acceptedResults.length > 0 ? EnumSet.copyOf(Arrays.asList(acceptedResults))
            : EnumSet.allOf(Resultat.class);

        NonCircularVisitorEvaluationCollector visitor = new NonCircularVisitorEvaluationCollector(new LeafNodeVisitorCondition(accepted, true));
        rootEvaluation.visit(rootEvaluation, visitor);
        return visitor.getCollected();
    }

    public List<RuleReasonRef> allOutcomes() {
        Set<Resultat> accepted = EnumSet.allOf(Resultat.class);

        NonCircularVisitorEvaluationCollector visitor = new NonCircularVisitorEvaluationCollector(new LeafNodeVisitorCondition(accepted, true));
        rootEvaluation.visit(rootEvaluation, visitor);
        return visitor.getCollected().stream()
            .map(Evaluation::getOutcome)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

}
