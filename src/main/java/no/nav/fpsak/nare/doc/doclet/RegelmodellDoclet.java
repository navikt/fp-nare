package no.nav.fpsak.nare.doc.doclet;

import java.io.File;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

public class RegelmodellDoclet implements Doclet {

    @SuppressWarnings("unused")
    private Locale locale;
    @SuppressWarnings("unused")
    private Reporter reporter;

    @Override
    public void init(Locale locale, Reporter reporter) {
        this.locale = locale;
        this.reporter = reporter;

    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
        return Collections.emptySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_10;
    }

    @Override
    public boolean run(DocletEnvironment docEnv) {
        System.out.println("Kjører Javadoc Doclet - " + getClass().getSimpleName());
        RegelModell regelModell = new RegelModell();
        RegelflytDoc regelflytDoc = new RegelflytDoc(docEnv, regelModell, getOutputLocation());
        try {
            Set<TypeElement> types = ElementFilter.typesIn(docEnv.getIncludedElements());
            types.stream().forEach(t -> regelflytDoc.process(t));
            File outputFileAdoc = new File(getOutputLocation(), "regler");
            new AsciidocMapper().writeTo(outputFileAdoc.toPath(), regelModell);
            return true;
        } catch (Error | RuntimeException e) {
            reporter.print(Kind.ERROR, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private File getOutputLocation() {
        File dir = new File(System.getProperty("destDir", "target/docs")); //$NON-NLS-1$ //$NON-NLS-2$
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IllegalStateException("Could not create output directory:" + dir); //$NON-NLS-1$
            }
        }
        return dir;
    }

}
