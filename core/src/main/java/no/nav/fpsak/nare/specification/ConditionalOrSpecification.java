package no.nav.fpsak.nare.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import no.nav.fpsak.nare.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

/**
 * Conditional Or specification, used to create a new specifcation that is the
 * AND of two other specifications.
 */
public class ConditionalOrSpecification<T> extends AbstractSpecification<T> {

	static class CondOrEntry<T> {
		Specification<T> testSpec;
		private Specification<T> flowSpec;

		CondOrEntry(Specification<T> testSpec, Specification<T> flowSpec) {
			super();
			this.testSpec = testSpec;
			this.flowSpec = flowSpec;
		}

		Specification<T> testSpec() {
			return testSpec;
		}

		Specification<T> flowSpec() {
			return flowSpec;
		}

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
	public Evaluation evaluate(final T t) {
		Optional<CondOrEntry<T>> firstMatch = this.conditionalEntries.stream().filter(coe -> {
			Evaluation testEval = coe.testSpec.evaluate(t);
			return Objects.equals(Resultat.JA, testEval.result());
		}).findFirst();

		if (firstMatch.isPresent()) {
			return firstMatch.get().flowSpec().evaluate(t);
		} else {
			return nei("{0} har ingen gyldige utganger", identifikator());
		}
	}

	@Override
	public String identifikator() {
		return this.specId;
	}

	@Override
	public String beskrivelse() {
		return this.beskrivelse;
	}

	@Override
	public RuleDescription ruleDescription() {
		String rootSpecId = identifikator();
		RuleDescription[] ruleDescriptions = conditionalEntries.stream().map(coe -> {
			return new RuleDescription(Operator.COND_OR, rootSpecId + "." + coe.testSpec().identifikator(),
					coe.testSpec().beskrivelse(), coe.testSpec().ruleDescription());
		}).toArray(RuleDescription[]::new);

		return new RuleDescription(Operator.COND_OR, identifikator(), beskrivelse(), ruleDescriptions);
	}

	public static <T> Builder<T> regel(String id, String beskrivelse) {
		return new Builder<T>(id, beskrivelse);
	}

	public static class Builder<T> {
		private final String specId;
		private final String beskrivelse;
		private final List<CondOrEntry<T>> conditionalEntries = new ArrayList<>();

		public Builder(String id, String beskrivelse) {
			this.specId = id;
			this.beskrivelse = beskrivelse;
		}
		
		public Builder<T> ellersHvis(Specification<T> testSpec, Specification<T> flowSpec) {
			this.conditionalEntries.add(new CondOrEntry<>(testSpec, flowSpec));
			return this;
		}

		public ConditionalOrSpecification<T> build() {
			return new ConditionalOrSpecification<>(specId, beskrivelse, conditionalEntries);
		}

	}

}
