package no.nav.fpsak.nare.doc.doclet;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.doc.RuleDocumentation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.Specification;

@RuleDocumentation(value = "hello.id", specificationReference = "https://google.com")
public class DummyRegelService implements RuleService<DummyRegelInput> {

    @SuppressWarnings("unchecked")
    @Override
    public Specification<DummyRegelInput> getSpecification() {
        return new LeafSpecification<>("hello.spec") {

            @Override
            public Evaluation evaluate(DummyRegelInput t) {
                throw new UnsupportedOperationException("not implemented");
            }
        };
    }

}
