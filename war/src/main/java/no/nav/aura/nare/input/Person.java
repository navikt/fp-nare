package no.nav.aura.nare.input;


import java.util.ArrayList;
import java.util.List;

public class Person{

    private final String name;
    private Rolle rolle;
    private final String yrke;
    private final int inntekt;
    private final String addresse;
    private boolean rettTilFp;

    private List<String> institusjonsOpphold;
    private final int mndArbeid;

    public Person(String name, Rolle rolle, String yrke, int inntekt, int mndArbeid, String addresse, boolean rettTilFp){
        this.name = name;
        this.rolle = rolle;
        this.yrke = yrke;
        this.inntekt = inntekt;
        this.mndArbeid = mndArbeid;
        this.addresse = addresse;
        this.rettTilFp = rettTilFp;
        this.institusjonsOpphold = new ArrayList<>();
    }

    public void addInstituasjonsOpphold(String institusjonsopphold){
        institusjonsOpphold.add(institusjonsopphold);
    }

    public List<String> getInstitusjonsOpphold(){
        return institusjonsOpphold;
    }

    public int getMndArbeid() {
        return mndArbeid;
    }

    public int getInntekt() {
        return inntekt;
    }

    public String getYrke() {
        return yrke;
    }

    public String getName() {
        return name;
    }

    public String getAddresse() {
        return addresse;
    }

    public Rolle getRolle() {
        return rolle;
    }


    public boolean harRettTilForeldrepenger() {
        return rettTilFp;
    }
}
