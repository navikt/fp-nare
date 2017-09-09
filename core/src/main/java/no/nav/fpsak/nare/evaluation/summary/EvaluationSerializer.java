package no.nav.fpsak.nare.evaluation.summary;

import com.google.gson.GsonBuilder;

import no.nav.fpsak.nare.evaluation.Evaluation;

public class EvaluationSerializer {

    public static String asJson(Evaluation evaluation) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(evaluation);

    }
}
