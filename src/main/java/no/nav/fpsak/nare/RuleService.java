package no.nav.fpsak.nare;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.Specification;

/**
 * Interface som definerer en regel tjeneste.
 * 
 * <p>
 * {@link #evaluer} metodene brukes til å kjøre et regelsett. Disse returnerer {@link Evaluation} som definer hvordan
 * kjøringen har godt. Delresultater beregnet underveis kan inkluderes.
 * 
 * {@link #getSpecification()} Definerer spesifikasjon for regelsettet, og benyttes til kjøring såvel som å generere
 * dokumentasjon på et regelsett.
 * 
 * @param <T> type av input
 */
public interface RuleService<T> {
    
    

    /**
     * Evaluer regel, all informasjon om kjøring returneres i Evaluation objektet.
     * 
     * @param input
     *            - input data til regeltjenesten.
     * @return {@link Evaluation} som beskriver kjøringen og eventuelle resultater regelen har generert for sporing og
     *         dokumentasjon.
     */
    default Evaluation evaluer(@SuppressWarnings("unused") T input) {
        throw new UnsupportedOperationException("Ikke implementert - implementer minst en av #evaluer metodene og bruk den");
    }

    /**
     * Evaluer regel, og returner eget output i angitt output container (til videre bruk i applikasjonen). Nare har ikke
     * noe forhold til denne outputten, men implementasjonen kan populere denne med delresultater som er interessante
     * for videre prosessering eller er enklere å forholde seg.
     * 
     * @param input
     *            - input data til regeltjenesten.
     * @param outputContainer
     *            - en egen-definert container for outputdata samlet fra regeltjenesten som applikasjonen kan benytte
     *            videre. Vil ikke bli brukt i dokumentasjon av kjøringe (se #Evaluation for det)
     * 
     * @return {@link Evaluation} som beskriver kjøringen og eventuelle resultater regelen har generert for sporing og
     *         dokumentasjon.
     */
    default Evaluation evaluer(@SuppressWarnings("unused") T input, @SuppressWarnings("unused") Object outputContainer) {
        throw new UnsupportedOperationException("Ikke implementert - implementer minst en av #evaluer metodene og bruk den");
    }

    /**
     * Angitt spesifikasjon for kjøring og dokumentasjon.
     * 
     * Input til denne trenge ikke være samme som til evaluer, men den kan ta et pre-prosessert objekt f.eks. for å
     * enklere håndtere reglene.
     */
    <V> Specification<V> getSpecification();
}
