package no.nav.aura.nare.evaluering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reason {

    private String id;
    private final String reason;
    private List<Resultat> results;
    private String operator;

    public Reason(String operator, String reason, Resultat... results) {
        this.id = "";
        this.reason = reason;
        this.results = Arrays.asList(results);
        this.operator = operator;


    }

    public Reason(String id, String reason) {
        this.id = id;
        this.operator = "";
        this.reason = reason;
        this.results = new ArrayList<>();
    }

    public String description() {
        switch (results.size()) {
            case 0:
                return id;
            case 1:
                return operator + results.get(0);
            case 2:
                return results.get(0) + " " + operator + " " + results.get(1);
            default:
                return reason;
        }
    }

    @Override
    public String toString(){
        return "_";
    }
}
