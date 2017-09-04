package no.nav.fpsak.nare.specification.modrekvote.regler;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.modrekvote.ModrekvoteUtfall;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;

public class HarArbeidetSisteMnd extends LeafSpecification<Soknad> {

    public static HarArbeidetSisteMnd harArbeidetSisteMnd(int mnd) {
        return new HarArbeidetSisteMnd(mnd);
    }

    private final int month;

    private HarArbeidetSisteMnd(int mnd) {
        this.month = mnd;
    }

    @Override
    public String beskrivelse() {
        return "Har dokumentert sammenhengende arbeid siste " + month + " mnd";
    }

    @Override
    public Evaluation evaluate(Soknad soknad) {
        int mndArbeid = soknad.getHovedsøker().getMndArbeid();
        return (mndArbeid > month)
                ? ja(ModrekvoteUtfall.UTFALL_01, "Person har jobbet {0} måneder, som er tilstrekkelig", mndArbeid)
                : nei(ModrekvoteUtfall.UTFALL_02, "Person er oppfort med {0} mnd arbeid. Dekker ikke kravet til {1} mnd med arbeid",
                        mndArbeid, month);
    }

    @Override
    public String identifikator() {
        return "FK_VK_10.x";
    }

}
