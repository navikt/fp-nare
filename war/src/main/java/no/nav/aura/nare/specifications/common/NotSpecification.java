package no.nav.aura.nare.specifications.common;


import no.nav.aura.nare.Reason;

/**
 * NOT decorator, used to create a new specifcation that is the inverse (IKKE) of the given spec.
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;

    public NotSpecification(final Specification<T> spec1) {
        this.spec1 = spec1;
    }

    public static NotSpecification ikke(final Specification spec1){
        return  new NotSpecification(spec1);
    }

    @Override
    public Evaluation evaluate(final T t) {
        Evaluation eval1 = spec1.evaluate(t);
        Evaluation evaluation = new Evaluation(eval1.result().not(), identifikator(), beskrivelse(),reason(eval1));
        evaluation.setChildren(eval1);
        return evaluation;
    }


    private Reason reason(Evaluation evaluation){
        return new Reason(identifikator(), "En begrynnelse", evaluation.result());
    }

    private String getReason(Evaluation eval1) {
        return "(" + eval1.result().not() + ": " + eval1.getReason()+ ")";
    }

    @Override
    public String identifikator() {
        return "IKKE";
    }

    @Override
    public String beskrivelse() {
        return "IKKE ";
    }
}
