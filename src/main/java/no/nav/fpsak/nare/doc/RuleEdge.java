package no.nav.fpsak.nare.doc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record RuleEdge(String source, String target, String role) {
    public RuleEdge(RuleNode source, RuleNode target, String edgeRole) {
        this(source.id(), target.id(), edgeRole);
    }
}