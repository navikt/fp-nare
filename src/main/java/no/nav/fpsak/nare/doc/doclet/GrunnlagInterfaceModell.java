package no.nav.fpsak.nare.doc.doclet;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import io.github.swagger2markup.markup.builder.MarkupDocBuilder;
import io.github.swagger2markup.markup.builder.MarkupTableColumn;
import jdk.javadoc.doclet.DocletEnvironment;

public class GrunnlagInterfaceModell implements MarkupOutput {
    private final List<GrunnlagInterfaceModell.Entry> entries = new ArrayList<>();
    private DocletEnvironment docEnv;

    static class Entry {
        Class<?> targetClass;
        String name;
        String classDoc;
        TypeElement typeElement;

        public Entry(TypeElement typeElement, String doc) throws ClassNotFoundException {
            this.typeElement = typeElement;
            this.classDoc = doc;
            this.name = typeElement.getQualifiedName().toString();
            this.targetClass = Class.forName(this.name);
        }
    }

    public GrunnlagInterfaceModell(DocletEnvironment docEnv) {
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
            ElementFilter.methodsIn(entry.typeElement.getEnclosedElements())
                    .stream()
                    .filter(m -> m.getModifiers().contains(Modifier.PUBLIC)
                            && m.getSimpleName().toString().startsWith("get")
                            && m.getReturnType().getKind() != TypeKind.VOID)
                    .forEach(m -> {
                        String methodName = m.getSimpleName().toString();
                        TypeMirror returnType = m.getReturnType();
                        String returnTypeName;
                        if (returnType.getKind() == TypeKind.DECLARED) {
                            TypeElement returnTypeElement = (TypeElement) docEnv.getTypeUtils()
                                    .asElement(m.getReturnType());
                            returnTypeName = returnTypeElement.getQualifiedName().toString();
                        } else {
                            returnTypeName = returnType.toString();
                        }
                        String docComment = docEnv.getElementUtils().getDocComment(m);
                        List<String> row = new ArrayList<>();
                        row.add(methodName);
                        row.add(returnTypeName);
                        row.add(docComment);
                        cells.add(row);
                    });
        });

        if (!cells.isEmpty())
            doc.tableWithColumnSpecs(columnSpecs, cells);
    }

    public void leggTil(TypeElement typeElement, String doc) throws ClassNotFoundException {
        entries.add(new GrunnlagInterfaceModell.Entry(typeElement, doc));
    }
}
