package no.nav.fpsak.nare.doc.doclet;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.lang.model.element.TypeElement;

import io.github.swagger2markup.markup.builder.MarkupBlockStyle;
import io.github.swagger2markup.markup.builder.MarkupDocBuilder;
import no.nav.fpsak.nare.DynamicRuleService;
import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.doc.RuleDocumentation;

@SuppressWarnings("restriction")
class RegelModell implements MarkupOutput {

    private final List<Entry> entries = new ArrayList<>();

    static class Entry {
        String qualifiedName;
        String doc;
        TypeElement element;
        private Class<?> targetClass;

        Entry(TypeElement element, String comment) throws ClassNotFoundException {
            this.qualifiedName = element.getQualifiedName().toString();
            this.element = element;
            this.doc = comment;
            this.targetClass = Class.forName(qualifiedName);
        }
    }

    private static Optional<String> getRuleServiceITypeName(Type[] interfaces) {
        Optional<String> getGenericInterfaceTypeName = Optional.empty();
        Optional<Type> ruleServiceType = Arrays.stream(interfaces)
            .filter(interfaze -> isRuleService(interfaze))
            .findFirst();

        if (ruleServiceType.isPresent()) {
            ParameterizedType pt = (ParameterizedType) ruleServiceType.get();
            Type[] ruleInput = pt.getActualTypeArguments();

            if (ruleInput.length == 1) {
                getGenericInterfaceTypeName = Optional.of(ruleInput[0].getTypeName());
            }
        }
        return getGenericInterfaceTypeName;
    }

    private static boolean isRuleService(Type interfaze) {
        return (interfaze.getTypeName().contains(RuleService.class.getTypeName())
            && ((ParameterizedType) interfaze).getRawType().getTypeName().equals(RuleService.class.getTypeName()))
            || (interfaze.getTypeName().contains(DynamicRuleService.class.getTypeName())
                && ((ParameterizedType) interfaze).getRawType().getTypeName().equals(DynamicRuleService.class.getTypeName()));
    }

    @Override
    public void apply(int sectionLevel, MarkupDocBuilder doc) {
        entries.forEach(entry -> {
            final String typeName = entry.qualifiedName;
            String[] splittetName = typeName.split("\\.");

            String title = splittetName[splittetName.length - 1];
            title = title.startsWith("Dokumentasjon") ? title.substring("Dokumentasjon".length()) : title;
            doc.sectionTitleLevel(sectionLevel, title);
            String beskrivelse = entry.doc;
            if (beskrivelse != null && !beskrivelse.trim().isEmpty())
                doc.block(beskrivelse, MarkupBlockStyle.PASSTHROUGH);

            final RuleDocumentation rdAnno = entry.element.getAnnotation(RuleDocumentation.class);
            if (rdAnno != null) {
                String specRef = rdAnno.specificationReference();
                String value = rdAnno.value();
                doc.text(value + ": " + specRef);
            }

            // Grunnlag
            Optional<String> genericTypeName = getGenericType(entry);
            if (genericTypeName.isPresent()) {
                doc.textLine("");
                doc.textLine("include::{generated}/" + genericTypeName.get() + ".adoc[]");
            }

            // Regel
            doc.textLine("");
            doc.textLine("include::{generated}/" + entry.qualifiedName + ".adoc[]");
        });
    }

    private Optional<String> getGenericType(Entry entry) {
        // TODO - bør skrives om til javax.lang.model slik at en slipper å ha Class på classpath for å kjøre her.
        Type[] interfaces = entry.targetClass.getGenericInterfaces();
        Optional<String> genericTypeName = getRuleServiceITypeName(interfaces);

        if (!genericTypeName.isPresent()) {
            Class<?> c = entry.targetClass.getSuperclass();
            Type[] superclassGenericTypes = c.getGenericInterfaces();
            if (superclassGenericTypes == null || superclassGenericTypes.length == 0) {
                superclassGenericTypes = new Type[] { c.getGenericSuperclass() };
            }
            genericTypeName = getRuleServiceITypeName(superclassGenericTypes);
        }
        return genericTypeName;
    }

    public void leggTil(TypeElement targetClass, String doc) throws ClassNotFoundException {
        this.entries.add(new Entry(targetClass, doc));
    }

}
