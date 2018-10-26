package no.nav.fpsak.nare.doc.doclet;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import io.github.swagger2markup.markup.builder.MarkupDocBuilder;
import io.github.swagger2markup.markup.builder.MarkupTableColumn;
import jdk.javadoc.doclet.DocletEnvironment;

class GrunnlagModell implements MarkupOutput {

    private final List<Entry> entries = new ArrayList<>();
    private DocletEnvironment docEnv;

    static class Entry {
        TypeElement targetClass;
        String name;
        String classDoc;

        Entry(TypeElement targetClass, String name, String classDoc) {
            this.targetClass = targetClass;
            this.name = name;
            this.classDoc = classDoc;
        }
    }

    public GrunnlagModell(DocletEnvironment docEnv) {
        this.docEnv = docEnv;
    }

    @Override
    public void apply(int sectionLevel, MarkupDocBuilder doc) {
        final int sl = sectionLevel++;
        doc.sectionTitleLevel(sl + 1, "INPUT");
        List<MarkupTableColumn> columnSpecs = new ArrayList<>();
        columnSpecs.add(new MarkupTableColumn("NAVN", false, 10));
        columnSpecs.add(new MarkupTableColumn("TYP", false, 10));
        columnSpecs.add(new MarkupTableColumn("BESKRIVELSE", false, 10));
        List<List<String>> cells = new ArrayList<>();

        entries.forEach(entry -> {
            List<VariableElement> fields = ElementFilter.fieldsIn(entry.targetClass.getEnclosedElements());
            fields.stream().forEach(f -> {
                List<String> row = new ArrayList<>();
                row.add(f.getSimpleName().toString());
                row.add(f.getEnclosingElement().asType().toString());
                row.add(docEnv.getElementUtils().getDocComment(f));
                cells.add(row);
            });
        });
        if (cells.size() > 0)
            doc.tableWithColumnSpecs(columnSpecs, cells);
    }

    public void leggTil(TypeElement targetClass, String name, String doc) {
        entries.add(new Entry(targetClass, name, doc));
    }
}
