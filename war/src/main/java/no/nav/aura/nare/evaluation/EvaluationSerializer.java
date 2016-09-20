package no.nav.aura.nare.evaluation;

import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;


public class EvaluationSerializer {

    public static String asXML(Evaluation evaluation){
        XStream xStream = new XStream();
        xStream.setMode(XStream.NO_REFERENCES);
        xStream.addImplicitCollection(AggregatedEvaluation.class, "children");
        xStream.aliasType("aggregatedEvaluation", AggregatedEvaluation.class);
        xStream.aliasType("singleEvaluation", SingleEvaluation.class);
        xStream.processAnnotations(new Class[]{AggregatedEvaluation.class, SingleEvaluation.class});
        return xStream.toXML(evaluation);
    }

    public static String asJson(Evaluation evaluation){
        return new GsonBuilder().setPrettyPrinting().create().toJson(evaluation);

    }
}
