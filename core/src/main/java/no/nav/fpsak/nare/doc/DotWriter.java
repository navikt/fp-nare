package no.nav.fpsak.nare.doc;

import java.io.Writer;
import java.util.Objects;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.specification.AndSpecification;
import no.nav.fpsak.nare.specification.ConditionalOrSpecification;
import no.nav.fpsak.nare.specification.OrSpecification;
import no.nav.fpsak.nare.specification.Specification;
/**
 * WIP - need som love
 */
public class DotWriter {

    public DotWriter() {
    }

    public void write(RuleService<?> ruleService, Writer writer) {
        // FIXME (FC)
    }

    public <T> String writeToString(RuleService<T> ruleService) {
        Specification<T> spec = ruleService.getSpecification();

        StringBuffer buf = new StringBuffer();
        buf.append("digraph g {\n");

        spec.visit(spec, (parentSpecification, childSpecification) -> {
            if(Objects.equals(parentSpecification.identifikator(),childSpecification.identifikator())) {
                return;
            }
            String line = "\"" + parentSpecification.identifikator()+ "\"" + " -> " + "\"" + childSpecification.identifikator()+ "\";\n";
            line += label(parentSpecification) + "\n";
            line += label(childSpecification) + "\n";
            buf.append(line);
        });

        buf.append("}");

        return buf.toString();
    }

    private String label(Specification spec) {
        if(spec instanceof AndSpecification) {
            return "\"" +spec.identifikator() +"\" [label=\"OG\"]";
        } else if (spec instanceof OrSpecification){
            return "\"" +spec.identifikator() +"\" [label=\"ELLER\"]";
        } else if (spec instanceof ConditionalOrSpecification){
            return "\"" +spec.identifikator() +"\" [label=\"COND_OR\"]";
        } else {
            return "";
        }
    }

}
