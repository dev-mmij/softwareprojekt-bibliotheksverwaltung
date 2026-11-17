package com.softwareprojekt.gruppe8.empfehlung.domain;

import jakarta.persistence.*;

/**
 * Einstellbarer Wert des Empfehlungsalgorithmus, der von Admins geaendert werden kann, um das Verhalten
 * der Empfehlungsauswahl zu beeinflussen.
 * @author Niklas Prell
 */

@Entity
public class Empfehlungsparameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parameterId;

    //Definition des gesamten Parameters, Zuordnung in der Datenbank ist als Kennung gespeichert
    @Enumerated(EnumType.STRING)
    @Column(name = "kennung", nullable = false, unique = true)
    private EmpfehlungsparameterDefinition definition;

    @Column(nullable = false)
    private double wert;

    @Column(nullable = false)
    private double standardwert;

    private String beschreibung;

    //Parameterloser Konstruktor fuer JPA
    protected Empfehlungsparameter() {
        super();
    }

    public Empfehlungsparameter(EmpfehlungsparameterDefinition definition, double standardwert, String beschreibung){
        definition.pruefe(standardwert);
        this.definition = definition;
        this.wert = standardwert; //neuer Empfehlungsparameter startet mit Standardwert
        this.standardwert = standardwert;
        this.beschreibung = beschreibung;
    }

    public Long getParameterId(){
        return parameterId;
    }
    public EmpfehlungsparameterDefinition getDefinition(){
        return definition;
    }
    public double getWert(){
        return wert;
    }
    public void setWert(double wert){
        definition.pruefe(wert);
        this.wert = wert;
    }
    public double getStandardwert(){
        return standardwert;
    }
    public String getBeschreibung(){
        return beschreibung;
    }
    public void setBeschreibung(String beschreibung){
        this.beschreibung = beschreibung;
    }
    public void aufStandardZuruecksetzen(){
        setWert(standardwert);
    }
}
