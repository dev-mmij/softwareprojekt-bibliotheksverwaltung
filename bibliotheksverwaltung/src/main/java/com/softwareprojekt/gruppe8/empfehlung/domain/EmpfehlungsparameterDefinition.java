package com.softwareprojekt.gruppe8.empfehlung.domain;

public enum EmpfehlungsparameterDefinition {
    STARTWERT_LIEBLINGSGENRE(0.0, 1.0, 0.01),
    DAUMEN_HOCH_GENRE(0.0, 1.0, 0.01),
    DAUMEN_HOCH_AUTOR(0.0, 1.0, 0.01),
    DAUMEN_RUNTER_GENRE(0.0, 1.0, 0.01),
    DAUMEN_RUNTER_AUTOR(0.0, 1.0, 0.01),
    BONUS_GEFOLGTER_AUTOR(0.0, 1.0, 0.01),
    GEWICHT_MIN(-1.0, 0.0, 0.01),
    GEWICHT_MAX(0.5, 1.0, 0.01),
    ANTEIL_AUTOR(0.0, 1.0, 0.01),
    ZUFALL_MAX(0.0, 0.5, 0.01),
    ENTDECKUNGSPLAETZE(0, 5, 1.0),
    MINDESTABSTAND_WIEDERHOLUNG(20, 50, 1.0);

    private final double minimum;
    private final double maximum;
    private final double schrittweite;

    EmpfehlungsparameterDefinition(double minimum, double maximum, double schrittweite){
        this.minimum = minimum;
        this.maximum = maximum;
        this.schrittweite = schrittweite;
    }

    public double getMinimum() {
        return minimum;
    }
    public double getMaximum() {
        return maximum;
    }
    public double getSchrittweite() {
        return schrittweite;
    }
    public double naechsterGueltigerWert(double wert){
        double gerundet = Math.round(wert/schrittweite) / (1/schrittweite);
        return Math.max(minimum, Math.min(maximum, gerundet));
    }
    public boolean istGueltig(double wert){
        return naechsterGueltigerWert(wert) == wert;
    }
    public void pruefe(double wert){
        if (!istGueltig(wert)){
            throw new IllegalArgumentException("Ungültiger Wert " + wert + " für " + name());
        }
    }

}
