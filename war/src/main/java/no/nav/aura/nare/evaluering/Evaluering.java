package no.nav.aura.nare.evaluering;

import java.util.Map;
import java.util.stream.Collectors;

public class Evaluering {

    private Map<String, Evaluation> vurderinger;

    private Evaluering(Map<String, Evaluation> vurderinger){
        this.vurderinger = vurderinger;
    }

    public static Evaluering resultat(Map<String, Evaluation> vurderinger){
        return new Evaluering(vurderinger);
    }

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
