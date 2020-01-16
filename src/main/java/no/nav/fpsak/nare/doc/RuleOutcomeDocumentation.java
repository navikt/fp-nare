package no.nav.fpsak.nare.doc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import no.nav.fpsak.nare.evaluation.Resultat;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface RuleOutcomeDocumentation {

    /** Kode som angir outcome. */
    String code();

    /** Outcome knyttet til {@link Resultat}. */
    Resultat result();

    /** Beskrivelse av outcome. */
    String description() default "";
}