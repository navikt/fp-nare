package no.nav.fpsak.nare;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import no.nav.fpsak.nare.specification.ComputationalIfSpecification;
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

    public Specification<V> beregningHvisRegel(Specification<V> testSpec, Specification<V> thenSpec, Specification<V> elseSpec) {
        return new ComputationalIfSpecification<>(testSpec, thenSpec, elseSpec);
    }

    public Specification<V> beregningsRegel(String id, String beskrivelse, Specification<V> spec1, Specification<V> spec2) {
        return new SequenceSpecification<>(id, beskrivelse, spec1, spec2);
    }

    public Specification<V> beregningsRegel(String id, String beskrivelse, Class<? extends DynamicRuleService<V>> spec1, V regelmodell, String argumentBeskrivelse, List<? extends Object> args1, Specification<V> spec2) {
        Objects.requireNonNull(spec1, "spec1");
        Objects.requireNonNull(args1, "args1");
        Objects.requireNonNull(spec2, "spec2");
        if (args1.isEmpty()) {
            throw new IllegalArgumentException("Argumentlisten kan ikke være tom");
        }
        List<Specification<V>> specs = new ArrayList<>();
        args1.forEach(arg -> {
            DynamicRuleService<V> ny;
            try {
                ny = spec1.newInstance();  // TODO (bts) bruk reflection med regelmodell som argument, fjerne constructor uten argument fra regelklassene
                ny.setRegelmodell(regelmodell);
                ny.medServiceArgument(new ServiceArgument(argumentBeskrivelse, arg));
                specs.add(ny.getSpecification());
            } catch (InstantiationException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new IllegalStateException("NARE feil");
            }
        });
        specs.add(spec2);
        return new SequenceSpecification<V>(id, beskrivelse, specs);
    }
        
//        Specification<V> denne = null;
//        try {
//            for (Object arg : args1) {
//                DynamicRuleService<V> ny;
//                ny = spec1.newInstance();  // TODO (bts) bruk reflection med regelmodell som argument, fjerne constructor uten argument fra regelklassene
//                ny.setRegelmodell(regelmodell);
//                ny.medArgument(arg);
//                if (denne == null) {
//                    denne = ny.getSpecification();
//                } else {
//                    Specification<V> neste = ny.getSpecification();
//                    denne = new SequenceSpecification<V>(id, beskrivelse, denne, neste);
//                }
//            }
//            return new SequenceSpecification<>(id, beskrivelse, denne, spec2);
//        } catch (InstantiationException | IllegalAccessException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        }
}
