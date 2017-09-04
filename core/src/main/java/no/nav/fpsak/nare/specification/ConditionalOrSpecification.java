package no.nav.fpsak.nare.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import no.nav.fpsak.nare.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.node.ConditionalOrEvaluation;

/**
 * Conditional Or specification, used to create a new specifcation that is the
 * AND of two other specifications.
 */
public class ConditionalOrSpecification<T> extends AbstractSpecification<T> {

    public static class Builder<T> {
        private final String specId;
        private final String beskrivelse;
        private final List<CondOrEntry<T>> conditionalEntries = new ArrayList<>();

        public Builder(String id, String beskrivelse) {
            this.specId = id;
            this.beskrivelse = beskrivelse;
        }

        public ConditionalOrSpecification<T> build() {
            return new ConditionalOrSpecification<>(specId, beskrivelse, conditionalEntries);
        }

        public Builder<T> ellersHvis(Specification<T> testSpec, Specification<T> flowSpec) {
            this.conditionalEntries.add(new CondOrEntry<>(testSpec, flowSpec));
            return this;
        }

    }

    static class CondOrEntry<T> {
        private final Specification<T> testSpec;
        private final Specification<T> flowSpec;

        CondOrEntry(Specification<T> testSpec, Specification<T> flowSpec) {
            this.testSpec = testSpec;
            this.flowSpec = flowSpec;
        }

        Specification<T> flowSpec() {
            return flowSpec;
        }

        Specification<T> testSpec() {
            return testSpec;
        }

    }

    public static <T> Builder<T> regel(String id, String beskrivelse) {
        return new Builder<T>(id, beskrivelse);
    }

    private final List<CondOrEntry<T>> conditionalEntries;

    private String specId;

    private String beskrivelse;

    public ConditionalOrSpecification(String specId, String beskrivelse, List<CondOrEntry<T>> conditionalEntries) {
        this.specId = specId;
        this.beskrivelse = beskrivelse;
        this.conditionalEntries = new ArrayList<>(conditionalEntries);
    }

    @Override
    public String beskrivelse() {
        return this.beskrivelse;
    }

    @Override
    public Evaluation evaluate(final T t) {
        AtomicReference<Evaluation> lastTestResult = new AtomicReference<Evaluation>();
        Optional<CondOrEntry<T>> firstMatch = this.conditionalEntries.stream().filter(coe -> {
            Evaluation testEval = coe.testSpec().evaluate(t);
            lastTestResult.set(testEval);
            // kun JA som skal fortsette, kast exception dersom testflyt returnerer Manuell?
            return Objects.equals(Resultat.JA, testEval.result());
        }).findFirst();

        if (firstMatch.isPresent()) {
            Evaluation testResult = lastTestResult.get();
            Evaluation flowResult = firstMatch.get().flowSpec().evaluate(t);
            return new ConditionalOrEvaluation(identifikator(), beskrivelse(), testResult, flowResult);
        } else {
            // varlse en kritisk feil? Er inne i en blindvei
            return nei(Operator.COND_OR.name(), "{0} har ingen gyldige utganger", identifikator());
        }
    }

    @Override
    public void visit(Specification<T> parentSpecification, SpecificationVisitor<T> visitor) {
        // TODO: riktig visit?
        for (CondOrEntry<T> entry : conditionalEntries) {
            entry.testSpec.visit(this, visitor);
            entry.flowSpec.visit(this, visitor);
        }
    }

    @Override
    public String identifikator() {
        return this.specId;
    }

    @Override
    public RuleDescription ruleDescription() {
        String rootSpecId = identifikator();
        RuleDescription[] ruleDescriptions = conditionalEntries.stream().map(coe -> {
            return new RuleDescription(Operator.COND_OR, rootSpecId + "->" + coe.testSpec().identifikator(),
                    coe.testSpec().beskrivelse(), coe.testSpec().ruleDescription());
        }).toArray(RuleDescription[]::new);

        return new RuleDescription(Operator.COND_OR, identifikator(), beskrivelse(), ruleDescriptions);
    }
    

}
