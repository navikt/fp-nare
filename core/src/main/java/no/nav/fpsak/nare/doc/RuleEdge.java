package no.nav.fpsak.nare.doc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RuleEdge {
    String source;
    String target;
    String role;

    public RuleEdge(RuleNode source, RuleNode target, String edgeRole) {
        this.source = source.id;
        this.target = target.id;
        role = edgeRole;
    }
}