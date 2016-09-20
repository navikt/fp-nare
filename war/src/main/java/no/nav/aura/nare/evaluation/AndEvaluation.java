package no.nav.aura.nare.evaluation;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class AndEvaluation extends AggregatedEvaluation {


    public AndEvaluation(String id, String ruleDescription, Evaluation... children) {
        super(Operator.AND, id, ruleDescription, children);
    }

    @Override
    public Resultat result() {
        return first().result().and(second().result());
    }

    @Override
    public String reason() {
        if (result().equals(Resultat.JA)){
            return "Tilfredstiller både " + first().ruleIdentification() + " og " + second().ruleIdentification();
        }else{
            return "Tilfredstiller ikke både " + first().ruleIdentification() + " og " + second().ruleIdentification();
        }



    }


}
