package no.nav.aura.nare;

import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.regelsettyper.Modrekvote;
import no.nav.aura.nare.specifications.common.Evaluation;
import no.nav.aura.nare.specifications.common.Specification;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static no.nav.aura.nare.Evaluering.resultat;


public class Ruleset {


    private Map<Regelbeskrivelse, Specification> specifications = new HashMap<>();

    private Specification specification;

    public static Ruleset modrekvote() {
        return new Modrekvote();
    }

    public void regel(String id, String regelbeskrivelse, Specification specification) {
        specifications.put(Regelbeskrivelse.id(id).beskrivelse(regelbeskrivelse), specification);
    }


    public Evaluation evaluer(Soknad soknad){
        return new Modrekvote().getModreKvote().evaluate(soknad);
    }

    public Evaluering vurder(Soknad soknad) {
        return resultat(specifications.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> entry.getValue().evaluate(soknad))));
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
