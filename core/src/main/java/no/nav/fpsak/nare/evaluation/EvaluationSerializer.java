package no.nav.fpsak.nare.evaluation;

import com.google.gson.GsonBuilder;

public class EvaluationSerializer {

    public static String asJson(Evaluation evaluation){
        return new GsonBuilder().setPrettyPrinting().create().toJson(evaluation);

    }
}
