package no.nav.fpsak.nare.doc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import no.nav.fpsak.nare.RuleService;

/**
 * Til hjelp ved dokumentasjon av {@link RuleService} implementasjoner.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Documented
public @interface RuleDocumentation {

    /** Regel referanse, eks. FP_VK_1 */
    String value();

    /** Referanse til definisjon av regelen. Eks. url til confluence eller lignende. */
    String specificationReference() default "";

    /** Referanse til lovhjemmel el. Kan være lovreferanse, lovdata url el. */
    String legalReference() default "";
    
    /** Potensielle outcomes for en regel. */
    RuleOutcomeDocumentation[] outcomes() default {};

}
