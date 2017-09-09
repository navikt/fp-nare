package no.nav.aura.nare.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Rolle;
import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Uttaksplan;
import no.nav.aura.nare.regelsettyper.Modrekvote;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSerializer;

@Component
@Path("/vurdering")
public class Nare {

    public static void main(String[] args) {

        /*
         * //Mor er hovedforsorger og enten hovedsøker eller medsøker
         * //Far er
         * 
         * Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800,"Klofta", false);
         * Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", false);
         * Person barn = new Person("Theo", Rolle.BARN,"Barn", 0, 0, "Oslo", false);
         * 
         * Evaluering evaluering = Ruleset
         * .hovedForsørger()
         * .vurder(Soknad.fodselSøknad(mor).forBarn(barn).medSøker(far));
         * 
         * System.out.println(evaluering.resultat());
         * System.out.println(evaluering.begrunnelse());
         * 
         * System.out.println(Ruleset.hovedForsørger().regelbeskrivelser());
         */

    }

    @GET
    public String getAnswer() {
        return "Almost there, what about adding 'applicationame'/'reponame'/'epic'?";
    }

    @GET
    @Path("/modrekvote")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response modrekvote(@DefaultValue("") @QueryParam("format") String format) throws Exception {
        Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800, "Klofta", true);
        Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", true);
        mor.setUttaksplan(Uttaksplan.SENERE);
        Evaluation evaluation = new Modrekvote().evaluer(Soknad.fodselSøknad(mor).medSøker(far));
        switch (format.toLowerCase()) {
            case "json":
            default:
                return Response.ok(EvaluationSerializer.asJson(evaluation))
                        .header("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8")
                        .build();
        }

    }
}
