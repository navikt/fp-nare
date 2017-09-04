package no.nav.fpsak.nare.doc;

import java.io.Writer;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.OrSpecification;
import no.nav.fpsak.nare.specification.Specification;

public class PlantUmlWriter {

    public PlantUmlWriter() {
    }

    public void write(RuleService<?> ruleService, Writer writer) {
        // FIXME (FC)
    }

    public <T> String writeToString(RuleService<T> ruleService) {
        Specification<T> spec = ruleService.getSpecification();

        StringBuffer buf = new StringBuffer();
        buf.append("@startuml");
        spec.visit(spec, (parentSpecification, childSpecification) -> {
            String line = "";
            if (childSpecification instanceof LeafSpecification) {
                line += ":" + childSpecification.identifikator();
            } else if (childSpecification instanceof OrSpecification) {
            }
            buf.append(line + "\n");
        });

        buf.append("@enduml");
        return buf.toString();
    }

}
