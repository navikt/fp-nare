package no.nav.aura.nare.evaluation;

import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;


public abstract class AggregatedEvaluation implements Evaluation {

    private final String ruleIdentifcation;
    private final String ruleDescription;
    private Operator operator;
    private Resultat resultat;
    private String reason;
    private List<Evaluation> children;


    protected AggregatedEvaluation(Operator operator, String id, String ruleDescription, Evaluation... children) {
        this.operator = operator;
        this.children = Arrays.asList(children);
        this.ruleIdentifcation = id;
        this.ruleDescription = ruleDescription;
        this.resultat = result();
        this.reason = reason();
    }

    protected Evaluation first() {
        return children.get(0);
    }
    protected Evaluation second() {
        return children.get(1);
    }


    @Override
    public String ruleDescription() {
        return  ruleDescription;
    }

    @Override
    public String ruleIdentification() {
        return ruleIdentifcation;
    }



    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
