package no.nav.fpsak.nare.specification.modrekvote.regler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.Ruleset;
import no.nav.fpsak.nare.ServiceArgument;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.RuleOutcome;
import no.nav.fpsak.nare.evaluation.node.SingleEvaluation;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSerializer;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSummary;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.Specification;

public class CountingpecificationTest {

    @Test
    public void skal_evaluere_regel_med_ellers_branch()  {
        var singleSpecification = new SingleRule();
        var evaluation = singleSpecification.evaluer(new MellomregnInt(1));

        assertThat(evaluation.result()).isEqualTo(Resultat.JA);
        System.out.println(EvaluationSerializer.asJson(evaluation));
        System.out.println(EvaluationSerializer.asJson(singleSpecification.getSpecification()));
        var evaluationSummary = new EvaluationSummary(evaluation);
        var outcome = hentOutcome(evaluationSummary);
        assertThat(outcome).contains(12);
    }

    @Test
    public void skal_evaluere_regel_med_hvis_branch()  {
        var singleSpecification = new SingleRule();
        var evaluation = singleSpecification.evaluer(new MellomregnInt(2));

        assertThat(evaluation.result()).isEqualTo(Resultat.JA);
        //System.out.println(EvaluationSerializer.asJson(evaluation));
        //System.out.println(EvaluationSerializer.asJson(singleSpecification.getSpecification()));
        var evaluationSummary = new EvaluationSummary(evaluation);
        var outcome = hentOutcome(evaluationSummary);
        assertThat(outcome).contains(24);
    }

    private Optional<Integer> hentOutcome(EvaluationSummary evaluationSummary) {
        return evaluationSummary.allOutcomes().stream()
                .map(o -> o instanceof RuleOutcome p ? p.calculated() : 0)
                .map(o -> o instanceof Integer i ? i : 0)
                .findFirst();
    }

    private static class MellomregnInt {
        private Integer carryon;
        MellomregnInt(Integer integer) {
            this.carryon = integer;
        }
    }


    private static class SingleRule implements RuleService<MellomregnInt> {

        /*
         * Del 1: Input + 1 + (3*1)
         * Del 2: Input = partall -> input*2 ellers input+1
         * Del 3: input * 2
         * Del 4: Sink som oppretter outcome
         */
        @Override
        public Evaluation evaluer(MellomregnInt data) {
            return getSpecification().evaluate(data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Specification<MellomregnInt> getSpecification() {
            var rs = new Ruleset<MellomregnInt>();
            var regel = rs.sekvensRegel()
                .neste(new AddOneLeaf())
                .forAlle("bokstav", List.of("A", "B", "C"), new AddOneLeaf())
                //.hvisEllers(new EvenLeaf(), new MultiplyByTwoLeaf(), new AddOneLeaf())
                .neste(rs.hvisRegel().hvis(new EvenLeaf(), new MultiplyByTwoLeaf()).ellers(new AddOneLeaf()))
                .neste(new MultiplyByTwoLeaf())
                .siste(new SinkLeaf());
            return regel;//rs.regel("Seq", "Seq", regel);
        }

    }

    private static class AddOneLeaf extends LeafSpecification<MellomregnInt> {
        private AddOneLeaf() {
            super("AddOne", "AddOne");
        }

        @Override
        public Evaluation evaluate(MellomregnInt input) {
            input.carryon = input.carryon + 1;
            var outcome = new RuleOutcome<>(input.carryon);
            return ja();
        }

        @Override
        public Evaluation evaluate(MellomregnInt input, ServiceArgument args) {
            var bokstav = Optional.ofNullable(args).filter(a -> "bokstav".equals(args.beskrivelse())).map(a -> (String)a.verdi()).orElse(null);
            input.carryon = input.carryon + 1;
            return ja();
        }
    }

    private static class MultiplyByTwoLeaf extends LeafSpecification<MellomregnInt> {
        private MultiplyByTwoLeaf() {
            super("TimesTwo", "TimesTwo");
        }

        @Override
        public Evaluation evaluate(MellomregnInt input) {
            input.carryon = input.carryon * 2;
            return ja();
        }
    }

    private static class EvenLeaf extends LeafSpecification<MellomregnInt> {
        private EvenLeaf() {
            super("CarryOn", "IsEvenNumber");
        }


        @Override
        public Evaluation evaluate(MellomregnInt input) {
            var desc = beskrivelse() + ": " + input.carryon;
            return new SingleEvaluation(input.carryon % 2 == 0  ? Resultat.JA : Resultat.NEI, identifikator(), desc, null);
        }
    }

    private static class SinkLeaf extends LeafSpecification<MellomregnInt> {

        private SinkLeaf() {
            super("Collect", "Collect");
        }

        @Override
        public Evaluation evaluate(MellomregnInt input) {
            var outcome = new RuleOutcome<>(input.carryon);
            return ja(outcome);
        }
    }
}
