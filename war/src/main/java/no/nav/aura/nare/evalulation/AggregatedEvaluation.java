package no.nav.aura.nare.evalulation;

import com.google.gson.GsonBuilder;
import no.nav.aura.nare.evaluering.Evaluering;
import no.nav.aura.nare.evaluering.Resultat;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;


public class AggregatedEvaluation implements Evaluation {

    private final String type;
    private final Integer ruleIdentifcation;
    private final String ruleDescription;
    private Operator operator;
    private Resultat resultat;
    private  String reason;
    private List<Evaluation> children;


    private AggregatedEvaluation(Operator operator, Evaluation... children){
        this.operator = operator;
        this.children = Arrays.asList(children);
        this.ruleIdentifcation = this.hashCode();
        this.ruleDescription=ruleDescription();
        this.resultat = result();
        this.reason = reason();
        this.type = "AGGREGATE";
    }

    public static AggregatedEvaluation andEvaluation(Evaluation eval1, Evaluation eval2){
        return new AggregatedEvaluation(Operator.AND,eval1, eval2);
    }

    public static AggregatedEvaluation orEvaluation(Evaluation eval1, Evaluation eval2){
        return new AggregatedEvaluation(Operator.OR, eval1,eval2);
    }

    public static AggregatedEvaluation notEvaluation(Evaluation eval){
        return new AggregatedEvaluation(Operator.NOT, eval,null);
    }

    private Evaluation eval1(){
        return children.get(0);
    }

    private Evaluation eval2(){
        return children.get(1);
    }



    @Override
    public Resultat result() {
        switch (operator){
            case AND: return eval1().result().and(eval2().result());
            case OR: return eval1().result().or(eval2().result());
            case NOT: return eval1().result().not();
            default: throw new RuntimeException("Unknown operatpr");
        }
    }

    @Override
    public String ruleDescription() {
        switch (operator){
            case AND: return eval1().ruleIdentification() + " OG " + eval2().ruleIdentification();
            case OR: return eval1().ruleIdentification() + " ELLER " + eval2().ruleIdentification();
            case NOT: return "NOT " + eval1().ruleIdentification();
            default: throw new RuntimeException("Unknown operator");
        }
    }

    @Override
    public String ruleIdentification() {
        return ruleIdentifcation.toString();
    }

    private String ruleIdentificationIfYes(Evaluation eval){
        return eval.result().equals(Resultat.JA) ? eval.ruleIdentification() : "";
    }

    private String ruleOrIdentification(){
        String eval1 = ruleIdentificationIfYes(eval1());
        String eval2 = ruleIdentificationIfYes(eval2());
        if (eval1.isEmpty()) return eval2;
        if (eval2.isEmpty()) return eval1;
        return  eval1 + " og " +  eval2;
    }

    @Override
    public String reason() {
        switch (operator){
            case AND: return "Tilfredstiller både " + eval1().ruleIdentification() + " og " + eval2().ruleIdentification();
            case OR: return  "Tilfredstiller " +  ruleOrIdentification();
            default: throw new RuntimeException("Unknown operator");
        }
    }



    @Override
    public String toString(){
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
