package no.nav.fpsak.nare;

import java.util.List;

import no.nav.fpsak.nare.specification.ConditionalOrSpecification;
import no.nav.fpsak.nare.specification.ConditionalOrSpecification.Builder;
import no.nav.fpsak.nare.specification.SequenceSpecification;
import no.nav.fpsak.nare.specification.Specification;

public class Ruleset<V> {

    public Specification<V> regel(String id, Specification<V> specification) {
        return specification.medID(id);
    }

    public Specification<V> regel(String id, String beskrivelse, Specification<V> specification) {
        return specification.medBeskrivelse(beskrivelse).medID(id);
    }

    public Builder<V> hvisRegel(String id, String beskrivelse) {
        return ConditionalOrSpecification.<V> regel(id, beskrivelse);
    }

    public Specification<V> beregningsRegel(Specification<V> spec1, Specification<V> spec2) {
        return new SequenceSpecification<>(spec1, spec2);
    }

    public Specification<V> beregningsRegel(Class<? extends RuleService<V>> spec1, List<Object> args1, Specification<V> spec2) {
        Specification<V> denne = null;
        try {
            for (Object arg : args1) {
                RuleService<V> ny;
                ny = spec1.newInstance();
                ny.medArgument(arg);
                if (denne == null) {
                    denne = ny.getSpecification();
                } else {
                    Specification<V> neste = ny.getSpecification();
                    denne = new SequenceSpecification<V>(denne, neste);
                }
            }
            return new SequenceSpecification<>(denne, spec2);
        } catch (InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
