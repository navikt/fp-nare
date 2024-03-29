package no.nav.fpsak.nare.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import no.nav.fpsak.nare.doc.BasicRuleDescription;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.RuleReasonRef;
import no.nav.fpsak.nare.evaluation.node.ConditionalElseEvaluation;
import no.nav.fpsak.nare.evaluation.node.ConditionalOrEvaluation;
import no.nav.fpsak.nare.json.JsonOutput;

/**
 * Conditional Or specification, used to create a new specifcation that is the
 * AND of two other specifications.
 */
public class ConditionalOrSpecification<T> extends AbstractSpecification<T> {

    private static final IllegalExit INVALID_EXIT = new IllegalExit(Operator.COND_OR.name(), "{0} har ingen gyldige utganger");

    public static class Builder<T> {
        private final List<CondOrEntry<T>> conditionalEntries = new ArrayList<>();
        private Specification<T> elseCondition;
        private final String id;
        private final String beskrivelse;

        public Builder() {
            this.id = "";
            this.beskrivelse = "";
        }

        public Builder(String id, String beskrivelse) {
            this.id = id;
            this.beskrivelse = beskrivelse;
        }

        public ConditionalOrSpecification<T> build() {
            ConditionalOrSpecification<T> spec = new ConditionalOrSpecification<>(conditionalEntries, elseCondition);
            spec.medID(id).medBeskrivelse(beskrivelse);
            return spec;
        }

        public Builder<T> hvis(Specification<T> testSpec, Specification<T> flowSpec) {
            this.conditionalEntries.add(new CondOrEntry<>(testSpec, flowSpec));
            return this;
        }

        public Builder<T> hvisIkke(Specification<T> testSpec, Specification<T> flowSpec) {
            Specification<T> inverseTestSpec = NotSpecification.ikke(testSpec);
            this.conditionalEntries.add(new CondOrEntry<>(inverseTestSpec, flowSpec));
            return this;
        }

        public ConditionalOrSpecification<T> ellers(Specification<T> flowSpec) {
            this.elseCondition = flowSpec;
            return this.build();
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

        @Override
        public String toString() {
            return JsonOutput.asJson(this);
        }

    }

    public static <T> Builder<T> regel() {
        return new Builder<>();
    }

    public static <V> Builder<V> regel(String id, String beskrivelse) {
        return new Builder<>(id, beskrivelse);
    }

    private final List<CondOrEntry<T>> conditionalEntries;
    private final Specification<T> elseCondition;

    public ConditionalOrSpecification(List<CondOrEntry<T>> conditionalEntries, Specification<T> elseCondition) {
        this.conditionalEntries = new ArrayList<>(conditionalEntries);
        if (elseCondition == null & conditionalEntries.size() == 1) {
            // bruk siste testspec som output dersom ikke er andre exit noder.
            this.elseCondition = conditionalEntries.get(0).testSpec;
        } else {
            this.elseCondition = elseCondition;
        }
    }

    /*
     * TODO: Vurder et binært/ternært expression-tree, som dette i realiteten er.
     *  OBS: testSpec som eval til NEI kastes - tenk ikke-komplett regelsporing vs regelversjonering
     */
    @Override
    public Evaluation evaluate(final T t) {
        AtomicReference<Evaluation> lastTestResult = new AtomicReference<>();
        Optional<CondOrEntry<T>> firstMatch = this.conditionalEntries.stream().filter(coe -> {
            var testEval = coe.testSpec().evaluate(t);
            lastTestResult.set(testEval);
            // kun JA som skal fortsette, kast exception dersom testflyt returnerer Manuell?
            return Objects.equals(Resultat.JA, testEval.result());
        }).findFirst();

        if (firstMatch.isPresent()) {
            var testResult = lastTestResult.get();
            var flowResult = firstMatch.get().flowSpec().evaluate(t);
            return new ConditionalOrEvaluation(identifikator(), beskrivelse(), testResult, flowResult);
        } else if (elseCondition != null) {
            var elseEvaluation = elseCondition.evaluate(t);
            return new ConditionalElseEvaluation(identifikator(), beskrivelse(), elseEvaluation);
        } else {
            // varlse en kritisk feil? Er inne i en blindvei
            return nei(INVALID_EXIT, identifikator());
        }
    }

    @Override
    public String identifikator() {
        return identifikatorIkkeTom().orElseGet(() -> {
            var condFirstEntry =  conditionalEntries.isEmpty() ? null : conditionalEntries.get(0);
            var condId = condFirstEntry != null ? ("HVIS " + condFirstEntry.testSpec.identifikator() + " SÅ  ... ") : "";
            var elseId = elseCondition != null ? ("ELLERS " + elseCondition.identifikator()) : "";
            return "(COND " + condId + elseId + ")";
        });
    }

    @Override
    public String beskrivelse() {
        return beskrivelseIkkeTom().orElse("(COND HVIS/SÅ/.../ELLERS)");
    }

    /*
     * TODO: Vurder et binært expression-tree, som dette i realiteten er. Dessuten flyr det ikke for komplekse test-clauses
     */
    @Override
    public RuleDescription ruleDescription() {
        var rootSpecId = identifikator();
        var ruleDescriptions = conditionalEntries.stream().map(coe -> {
            return new BasicRuleDescription(Operator.AND, rootSpecId + "\u2192" + coe.testSpec().identifikator(),
                    coe.testSpec().beskrivelse(), coe.flowSpec.ruleDescription());
        }).collect(Collectors.toList());

        List<RuleDescription> allRuleDescriptions = new ArrayList<>(ruleDescriptions);
        if (elseCondition != null) {
            RuleDescription elseRuleDescription = elseCondition.ruleDescription();
            allRuleDescriptions.add(elseRuleDescription);
        }

        RuleDescription[] arrayRuleDesc = allRuleDescriptions.toArray(new RuleDescription[allRuleDescriptions.size()]);

        return new SpecificationRuleDescription(Operator.COND_OR, identifikator(), beskrivelse(), arrayRuleDesc);
    }

    private static record IllegalExit(String reasonCode, String reason) implements RuleReasonRef {
        @Override
        public String getReasonTextTemplate() {
            return reason;
        }

        @Override
        public String getReasonCode() {
            return reasonCode;
        }
    }

}
