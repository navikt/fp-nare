package no.nav.fpsak.nare.doc.doclet;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import io.github.swagger2markup.markup.builder.MarkupDocBuilder;
import io.github.swagger2markup.markup.builder.MarkupDocBuilders;
import io.github.swagger2markup.markup.builder.MarkupLanguage;

class AsciidocMapper {

    void writeTo(Path path, MarkupOutput model) {
        MarkupDocBuilder documentBuilder = MarkupDocBuilders.documentBuilder(MarkupLanguage.ASCIIDOC);

        model.apply(1, documentBuilder);

        documentBuilder.writeToFile(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
