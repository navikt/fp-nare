package no.nav.aura.nare.evaluering;

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

    public Evaluation(Resultat result, String id, String description, String reason, Object... stringformatArguments) {
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
        return "Evaluering: " + result + "\n\tBegrunnelse: " + "reason" + "";
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
        System.out.println(prefix + (isTail ? "└── " : "├── ") + result);
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1).print(prefix + (isTail ? "    " : "│   "), true);
        }
    }
}
