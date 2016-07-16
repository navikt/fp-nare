package no.nav.aura.nare;


import no.nav.aura.nare.regelsettyper.Modrekvote;
import no.nav.aura.nare.specifications.Specification;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class Ruleset {

    private Map<Regelbeskrivelse, Specification> specifications = new HashMap<>();

    private Specification specification;

    public static Specification modrekvote() {
        return new Modrekvote().getModreKvote();
    }

    public void regel(String id, String regelbeskrivelse, Specification specification) {
        specifications.put(Regelbeskrivelse.id(id).beskrivelse(regelbeskrivelse), specification);
    }


    public String regelbeskrivelser() {
        return prettyifyMap(specifications.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> entry.getValue().beskrivelse())));
    }

    private String prettyifyMap(Map<String,String> map){
        return map.entrySet()
                .stream()
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .map(entry -> entry.getKey() + "\t" + entry.getValue()+"\n")
                .collect(Collectors.joining(""));
    }
}
