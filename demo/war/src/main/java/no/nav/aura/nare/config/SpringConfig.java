package no.nav.aura.nare.config;

import no.nav.aura.nare.RootPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = RootPackage.class, excludeFilters = @Filter(Configuration.class))
public class SpringConfig {

}
