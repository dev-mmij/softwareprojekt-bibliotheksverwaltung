package com.softwareprojekt.gruppe8.empfehlung.domain;

import jakarta.persistence.*;

/**
 * @author Niklas Prell
 *
 * Einstellbarer Wert des Empfehlungsalgorithmus, der von Admins geaendert werden kann, um das Verhalten
 * der Empfehlungsauswahl zu beeinflussen.
 */

@Entity
public class Empfehlungsparameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parameterId;

    //Eindeutiger technischer Name eines Parameters
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double wert;

    @Column(nullable = false)
    private double standardwert;

    private String beschreibung;

    //Parameterloser Konstruktur fuer JPA
    protected Empfehlungsparameter() {
        super();
    }

    public Empfehlungsparameter(String name, double standardwert, String beschreibung){
        this.name = name;
        this.wert = standardwert; //neuer Empfehlungsparameter startet mit Standardwert
        this.standardwert = standardwert;
        this.beschreibung = beschreibung;
    }

    public Long getParameterId(){
        return parameterId;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public double getWert(){
        return wert;
    }
    public void setWert(double wert){
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
        this.wert = standardwert;
    }
}
