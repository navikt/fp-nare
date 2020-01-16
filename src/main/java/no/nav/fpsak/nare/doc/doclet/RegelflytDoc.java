package no.nav.fpsak.nare.doc.doclet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

import javax.lang.model.element.TypeElement;

import jdk.javadoc.doclet.DocletEnvironment;
import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.doc.RuleDescriptionDigraph;
import no.nav.fpsak.nare.doc.RuleDocumentation;
import no.nav.fpsak.nare.doc.RuleDocumentationGrunnlag;

public class RegelflytDoc {

    private RegelModell regelModell;
    private DocletEnvironment docEnv;
    private File outputLocation;

    public RegelflytDoc(DocletEnvironment docEnv, RegelModell regelModell, File outputLocation) {
        this.docEnv = docEnv;
        this.regelModell = regelModell;
        this.outputLocation = outputLocation;
    }

    public boolean accept(TypeElement cls) {
        try {
            boolean accept = (hasRuleDocumentationAnnotation(cls) && (hasRuleServiceInterface(cls)))
                    || isRuleDocumentationGrunnlagClass(cls) || isRuleDocumentationGrunnlagInterface(cls);

            return accept;

        } catch (Exception e) {
            // Do nothing
        }
        return false;
    }

    private boolean hasRuleDocumentationAnnotation(TypeElement cls) {
        return hasAnnotation(cls, RuleDocumentation.class);
    }

    private boolean isRuleDocumentationGrunnlagClass(TypeElement cls) {
        return hasAnnotation(cls, RuleDocumentationGrunnlag.class);
    }

    private boolean isRuleDocumentationGrunnlagInterface(TypeElement cls) {
        return hasAnnotation(cls, RuleDocumentationGrunnlag.class);
    }

    private boolean hasAnnotation(TypeElement cls, Class<? extends Annotation> annotation) {
        return cls.getAnnotation(annotation) != null;
    }

    private boolean hasRuleServiceInterface(TypeElement cls) throws ClassNotFoundException {
        Class<?> clazz = this.getClass().getClassLoader().loadClass(cls.getQualifiedName().toString());
        return Arrays.stream(clazz.getInterfaces())
                .filter(interfaze -> interfaze.getTypeName().equals(RuleService.class.getName())).count() == 1;
    }

    public void process(TypeElement element) {
        if (!accept(element)) {
            return;
        }
        TypeElement e = element;
        try {
            Class<?> cls = Class.forName(e.getQualifiedName().toString());
            if (isRuleDocumentationGrunnlagInterface(e)) {
                processGrunnlagInterface(e);

            } else if (isRuleDocumentationGrunnlagClass(e)) {
                processVilkårGrunnlag(e);

            } else if (hasRuleDocumentationAnnotation(e)) {
                processRegelflytForRuleService(cls, e);
                regelModell.leggTil(element, docEnv.getElementUtils().getDocComment(element));
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void processVilkårGrunnlag(TypeElement typeElement) {
        GrunnlagModell resultat = new GrunnlagModell(docEnv);
        String name = typeElement.getQualifiedName().toString();
        resultat.leggTil(typeElement, name, docEnv.getElementUtils().getDocComment(typeElement));
        File outputFileAdoc = new File(getOutputLocation(), name);
        new AsciidocMapper().writeTo(outputFileAdoc.toPath(), resultat);
    }

    private void processGrunnlagInterface(TypeElement typeElement) throws ClassNotFoundException {
        GrunnlagInterfaceModell resultat = new GrunnlagInterfaceModell(docEnv);
        resultat.leggTil(typeElement, docEnv.getElementUtils().getDocComment(typeElement));

        String name = typeElement.getQualifiedName().toString();
        File outputFileAdoc = new File(getOutputLocation(), name);
        new AsciidocMapper().writeTo(outputFileAdoc.toPath(), resultat);
    }

    private void processRegelflytForRuleService(Class<?> targetClass, TypeElement doc)
            throws InstantiationException, IllegalAccessException, FileNotFoundException, UnsupportedEncodingException,
            InvocationTargetException, NoSuchMethodException {
        Optional<Constructor<?>> defaultConstructor = Arrays.stream(targetClass.getConstructors())
                .filter(c -> c.getParameterCount() == 0)
                .findFirst();
        if (defaultConstructor.isPresent()) {
            @SuppressWarnings("rawtypes")
            RuleService ruleService = (RuleService) targetClass.getDeclaredConstructor().newInstance();
            RuleDescription ruleDescription = ruleService.getSpecification().ruleDescription();
            createRegelflytAdocFile(ruleDescription, doc);
        } else {
            System.out.println(">>>>>>>>> " + targetClass + " MANGLER DEFAULT KONSTRUKTUR");
        }
    }

    private void createRegelflytAdocFile(RuleDescription ruleDescription, TypeElement element)
            throws FileNotFoundException, UnsupportedEncodingException {
        RuleDescriptionDigraph digraph = new RuleDescriptionDigraph(ruleDescription);
        String json = digraph.toJson();
        String name = element.getQualifiedName().toString();
        File outputFile = new File(getOutputLocation(), name + ".json");
        try (PrintWriter w = new PrintWriter(outputFile, "UTF-8")) {
            w.write(json);
        }

        RegelflytModell resultat = new RegelflytModell();
        resultat.leggTil(name);

        File outputFileAdoc = new File(getOutputLocation(), name);
        new AsciidocMapper().writeTo(outputFileAdoc.toPath(), resultat);
    }

    private File getOutputLocation() {
        return outputLocation;
    }

}
