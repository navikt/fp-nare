package no.nav.fpsak.nare;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.EvaluationSpecification;

public interface RuleService<T> {
	
	Evaluation evaluer(T data);
	
	EvaluationSpecification<T> getSpecification();
}
