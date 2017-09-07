package no.nav.aura.nare.regler;

import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.regelsettyper.ModrekvoteUtfall;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.LeafSpecification;

public class HarArbeidetSisteMnd extends LeafSpecification<Soknad> {

    public static HarArbeidetSisteMnd harArbeidetSisteMnd(int mnd) {
        return new HarArbeidetSisteMnd(mnd);
    }

    private final int month;

    private HarArbeidetSisteMnd(int mnd) {
        super("FK_VK_10.x");
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
                ? ja(ModrekvoteUtfall.ARBEIDET_TILSTREKKELIG, mndArbeid)
                : nei(ModrekvoteUtfall.ARBEIDET_UTILSTREKKELIG, mndArbeid, month);
    }

}
