package no.nav.fpsak.nare;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Til hjelp ved dokumentasjon av {@link RuleService} implementasjoner.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Documented
public @interface RuleDocumentation {

    /** Regel referanse, eks. FK_VK_1 */
    String value();

    /** Referanse til definisjon av regelen. Eks. url til confluence eller lignende. */
    String specificationReference() default "";
    
    /** Referanse til lovhjemmel el. Kan være lovreferanse, lovdata url el.*/
    String legalReference() default "";
}
