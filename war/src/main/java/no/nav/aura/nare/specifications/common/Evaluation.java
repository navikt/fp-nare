package no.nav.aura.nare.specifications.common;

import no.nav.aura.nare.Reason;
import no.nav.aura.nare.Resultat;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class Evaluation implements Serializable {

    private String id;
    private String description;
    private Resultat result;
    private Reason reason;
    private List<Evaluation> children = new ArrayList<>();

    private Evaluation(Resultat result) {
        this.result = result;
    }

    public Evaluation(Resultat result,String id, String description, String reason, Object... stringformatArguments) {
        this(result);
        this.id = id;
        this.description = description;
        this.reason = new Reason(id, MessageFormat.format(reason, stringformatArguments));
    }

    public Evaluation(Resultat result, String id, String description, Reason reason) {
        this(result);
        this.id = id;
        this.description = description;
        this.reason = reason;

    }


    public Resultat result() {
        return result;
    }


    public Reason getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "Evaluering: " + result + "\n\tBegrunnelse: " + reason + "";
    }

    /* public static final Evaluation yes(String id, String description,String reason, Object... stringformatArguments) {
         return new Evaluation(Resultat.JA, id, description, reason, stringformatArguments);
     }

     public static final Evaluation no(String id, String description,String reason, Object... stringformatArguments) {
         return new Evaluation(Resultat.NEI, id, description, reason, stringformatArguments);
     }

     public static final Evaluation manual(String reason, String id, String description,Object... stringformatArguments) {
         return new Evaluation(Resultat.MANUELL_BEHANDLING, id, description, reason, stringformatArguments);
     }
 */
    public static final Evaluation yes(String id, String description, Reason reason) {
        return new Evaluation(Resultat.JA, id, description, reason);
    }

    public static final Evaluation no(String id, String description, Reason reason) {
        return new Evaluation(Resultat.NEI, id, description, reason);
    }

    public static final Evaluation manual(String id, String description, Reason reason) {
        return new Evaluation(Resultat.MANUELL_BEHANDLING, id, description, reason);
    }

    public void setChildren(Evaluation... evaluations) {
        this.children = Arrays.asList(evaluations);
    }

    public List<Evaluation> getChildren() {
        return children;
    }


    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + result + (children.size() > 0 ? " (" + id + " " + result().not() + ")" : ": " + id + " - " + description));
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1).print(prefix + (isTail ? "    " : "│   "), true);
        }
    }
}
