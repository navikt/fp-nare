package no.nav.fpsak.nare.doc.doclet;

import io.github.swagger2markup.markup.builder.MarkupDocBuilder;

public interface MarkupOutput {

    void apply(int sectionLevel, MarkupDocBuilder doc);
}
