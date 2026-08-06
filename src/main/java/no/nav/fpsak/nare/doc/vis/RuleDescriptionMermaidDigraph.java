package no.nav.fpsak.nare.doc.vis;

import no.nav.fpsak.nare.doc.RuleEdge;
import no.nav.fpsak.nare.doc.RuleNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SequencedCollection;

public record RuleDescriptionMermaidDigraph(ArrayList<RuleNode> nodes, ArrayList<RuleEdge> edges) {

    public RuleDescriptionMermaidDigraph(SequencedCollection<RuleNode> nodes, Collection<RuleEdge> edges) {
        this(new ArrayList<>(nodes), new ArrayList<>(edges));
    }

}
