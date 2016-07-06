package no.nav.aura.nare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reason {

    private final String reason;
    private List<Resultat> results;
    private String id;

    public Reason(String id, String reason, Resultat... results) {
        this.reason = reason;
        this.results = Arrays.asList(results);
        this.id = id;
    }

    public Reason(String id, String reason){
        this.reason = reason;
        this.id = id;
        this.results = new ArrayList<>();
    }
}
