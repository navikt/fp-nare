package no.nav.aura.nare;

import no.nav.aura.nare.input.Familie;
import no.nav.aura.nare.regelsettyper.Hovedforsorger;
import no.nav.aura.nare.specifications.common.Specification;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static no.nav.aura.nare.Evaluering.resultat;


public class Regelsett {


    private Map<Regelbeskrivelse, Specification> specifications = new HashMap<>();

    public static Regelsett hovedForsorger() {
        return new Hovedforsorger();
    }

    public void regel(Integer id, String regelbeskrivelse, Specification specification) {
        specifications.put(Regelbeskrivelse.id(id).beskrivelse(regelbeskrivelse), specification);
    }

    public Evaluering vurder(Familie familie) {
        return resultat(specifications.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> entry.getValue().evaluate(familie))));
    }

    public String regelbeskrivelser() {
        return prettyifyMap(specifications.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> entry.getValue().getDescription())));
    }

    private String prettyifyMap(Map<String,String> map){
        return map.entrySet()
                .stream()
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .map(entry -> entry.getKey() + "\t" + entry.getValue()+"\n")
                .collect(Collectors.joining(""));
    }

}
