package com.softwareprojekt.gruppe8.empfehlung.domain;

public enum EmpfehlungsparameterDefinition {
    STARTWERT_LIEBLINGSGENRE(0.0, 1.0, false),
    DAUMEN_HOCH_GENRE(0.0, 1.0, false),
    DAUMEN_HOCH_AUTOR(0.0, 1.0, false),
    DAUMEN_RUNTER_GENRE(0.0, 1.0, false),
    DAUMEN_RUNTER_AUTOR(0.0, 1.0, false),
    BONUS_GEFOLGTER_AUTOR(0.0, 1.0, false),
    GEWICHT_MIN(-1.0, 0.0, false),
    GEWICHT_MAX(0.5, 1.0, false),
    ANTEIL_AUTOR(0.0, 1.0, false),
    ZUFALL_MAX(0.0, 0.5, false),
    ENTDECKUNGSPLAETZE(0, 5, true),
    MINDESTABSTAND_WIEDERHOLUNG(20, 50, true);

    private final double minimum;
    private final double maximum;
    private final boolean istGanzzahl;

    EmpfehlungsparameterDefinition(double minimum, double maximum, boolean istGanzzahl){
        this.minimum = minimum;
        this.maximum = maximum;
        this.istGanzzahl = istGanzzahl;
    }

    public double getMinimum() {
        return minimum;
    }
    public double getMaximum() {
        return maximum;
    }
    public boolean istGanzzahl() {
        return istGanzzahl;
    }
    public boolean istGueltig(double wert){
        boolean istPassenderZahlentyp = !istGanzzahl || (int)wert == wert;
        boolean istPassenderBereich = wert >= minimum && wert <= maximum;
        return  istPassenderZahlentyp && istPassenderBereich;
    }

}
