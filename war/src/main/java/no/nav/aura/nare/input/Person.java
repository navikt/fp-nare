package no.nav.aura.nare.input;


public class Person{

    private final String name;
    private final String yrke;
    private final int inntekt;

    private final int mndArbeid;

    public Person(String name, Rolle rolle, String yrke, int inntekt, int mndArbeid){
        this.name = name;
        this.yrke = yrke;
        this.inntekt = inntekt;
        this.mndArbeid = mndArbeid;
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




}
