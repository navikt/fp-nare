package no.nav.aura.nare.saveforlater;

import no.nav.aura.nare.evaluation.Evaluation;
import no.nav.aura.nare.evaluation.Resultat;

import java.util.Map;
import java.util.stream.Collectors;

public class MapReduce {

    private Map<String, Evaluation> vurderinger;


    public Resultat resultat (){
        return vurderinger.values().stream()
                .map(eval -> eval.result())
                .reduce( (a,b) -> a.and(b)).get();
    }

    public String begrunnelse(){
        return vurderinger.entrySet().stream()
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .map(entry -> entry.getKey() + "\t" + entry.getValue()+"\n")
                .collect(Collectors.joining(""));
    }


}
