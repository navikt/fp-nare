package no.nav.aura.nare.evalulation;

import com.google.gson.GsonBuilder;
import no.nav.aura.nare.evaluering.Resultat;


public class AggregatedEvaluation implements Evaluation {


    private final Integer identification;
    private Operator operator;
    private Resultat resultat;
    private  String reason;
    private final Evaluation eval1;
    private final Evaluation eval2;

    private AggregatedEvaluation(Operator operator, Evaluation eval1, Evaluation eval2){
        this.operator = operator;
        this.eval1 = eval1;
        this.eval2 = eval2;
        this.identification = this.hashCode();
        this.resultat = result();
        this.reason = reason();
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

    @Override
    public Resultat result() {
        switch (operator){
            case AND: return eval1.result().and(eval2.result());
            case OR: return eval1.result().or(eval2.result());
            case NOT: return eval1.result().not();
            default: throw new RuntimeException("Unknown operatpr");
        }
    }

    @Override
    public String ruleDescription() {
        switch (operator){
            case AND: return eval1.ruleDescription() + " AND " + eval2.ruleDescription();
            case OR: return eval1.ruleDescription() + " OR " + eval2.ruleDescription();
            case NOT: return "NOT " + eval1.ruleDescription();
            default: throw new RuntimeException("Unknown operatpr");
        }
    }

    @Override
    public String ruleIdentification() {
        return identification.toString();

    }

    @Override
    public String reason() {
        switch (operator){
            case AND: return "Satisfies both " + eval1.ruleIdentification() + " and " + eval2.ruleIdentification();
            case OR: return  "Satisfies at least " + eval1.ruleIdentification() +"(" + eval1.result()  +") or " + eval2.ruleIdentification() + " (" + eval2.result() + ")";
            case NOT: return "Satisfied the opposite of " + eval1.ruleIdentification() + " (" + eval1.reason() + ")";
            default: throw new RuntimeException("Unknown operatpr");
        }
    }

    @Override
    public String toString(){
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
