package no.nav.fpsak.nare;

import java.util.List;
import java.util.Objects;

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

    public Specification<V> beregningsRegel(Class<? extends DynamicRuleService<V>> spec1, V regelmodell, List<? extends Object> args1, Specification<V> spec2) {
        Objects.requireNonNull(spec1, "spec1");
        Objects.requireNonNull(args1, "args1");
        Objects.requireNonNull(spec2, "spec2");
        if (args1.isEmpty()) {
            throw new IllegalArgumentException("Argumentlisten kan ikke være tom");
        }
        Specification<V> denne = null;
        try {
            for (Object arg : args1) {
                DynamicRuleService<V> ny;
                ny = spec1.newInstance();  // TODO (bts) bruk reflection med regelmodell som argument, fjerne constructor uten argument fra regelklassene
                ny.setRegelmodell(regelmodell);
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
