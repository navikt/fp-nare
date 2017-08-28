package no.nav.fpsak.nare.evaluation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public abstract class AggregatedEvaluation implements Evaluation {

    private  String ruleIdentification;
    private  String ruleDescription;
    private Operator operator;
    private Resultat resultat;

    private String reason;
    private List<Evaluation> children;

    protected AggregatedEvaluation(Operator operator, String id, String ruleDescription, Evaluation... children) {
        this.operator = operator;
        this.children = Arrays.asList(children);
        this.ruleIdentification = id;
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
    
    public List<Evaluation> children(){
    	return Collections.unmodifiableList(children);
    }


    @Override
    public String ruleDescription() {
        return ruleDescription;
    }

    @Override
    public String ruleIdentification() {
        return ruleIdentification;
    }

}
