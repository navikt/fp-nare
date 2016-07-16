package no.nav.aura.nare.evaluation;

import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;


public class AggregatedEvaluation implements Evaluation {

    private final Integer ruleIdentifcation;
    private final String ruleDescription;
    private Operator operator;
    private Resultat resultat;
    private String reason;
    private List<Evaluation> children;


    private AggregatedEvaluation(Operator operator, Evaluation... children) {
        this.operator = operator;
        this.children = Arrays.asList(children);
        this.ruleIdentifcation = this.hashCode();
        this.ruleDescription = ruleDescription();
        this.resultat = result();
        this.reason = reason();
    }

    public static AggregatedEvaluation andEvaluation(Evaluation eval1, Evaluation eval2) {
        return new AggregatedEvaluation(Operator.AND, eval1, eval2);
    }

    public static AggregatedEvaluation orEvaluation(Evaluation eval1, Evaluation eval2) {
        return new AggregatedEvaluation(Operator.OR, eval1, eval2);
    }

    public static AggregatedEvaluation notEvaluation(Evaluation eval) {
        return new AggregatedEvaluation(Operator.NOT, eval);
    }

    private Evaluation first() {
        return children.get(0);
    }

    private Evaluation second() {
        return children.get(1);
    }


    @Override
    public Resultat result() {
        switch (operator) {
            case AND:
                return first().result().and(second().result());
            case OR:
                return first().result().or(second().result());
            case NOT:
                return first().result().not();
            default:
                throw new RuntimeException("Unknown operatpr");
        }
    }

    @Override
    public String ruleDescription() {
        switch (operator) {
            case AND:
                return first().ruleIdentification() + " OG " + second().ruleIdentification();
            case OR:
                return first().ruleIdentification() + " ELLER " + second().ruleIdentification();
            case NOT:
                return "NOT " + first().ruleIdentification();
            default:
                throw new RuntimeException("Unknown operator");
        }
    }

    @Override
    public String ruleIdentification() {
        return ruleIdentifcation.toString();
    }

    private String ruleOrIdentification() {
        String firstID = first().result().equals(Resultat.JA) ? first().ruleIdentification() : "";
        String secondID = second().result().equals(Resultat.JA) ? second().ruleIdentification() : "";
        if (firstID.isEmpty()) return secondID;
        if (secondID.isEmpty()) return firstID;
        return firstID + " OG " + secondID;
    }

    @Override
    public String reason() {
        switch (operator) {
            case AND:
                return "Tilfredstiller både " + first().ruleIdentification() + " og " + second().ruleIdentification();
            case OR:
                return "Tilfredstiller " + ruleOrIdentification();
            case NOT:
                return "Tilfredstiller det motsatte av " + first().ruleIdentification();
            default:
                throw new RuntimeException("Unknown operator");
        }
    }


    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
