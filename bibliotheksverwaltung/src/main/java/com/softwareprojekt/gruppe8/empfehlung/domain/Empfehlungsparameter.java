package com.softwareprojekt.gruppe8.empfehlung.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Empfehlungsparameter {

    //Primaerschluessel
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empfehlungsparameter_id;

    //Nichtschluesselattribute
    private String name;
    private double wert;
    private double standardwert;
    private String beschreibung;

    //Parameterloser Konstruktur
    public Empfehlungsparameter() {
        super();
    }

    //Parametrisierter Konstruktor
    public Empfehlungsparameter(String name, double wert, double standardwert, String beschreibung){
        this.name = name;
        this.wert = wert;
        this.standardwert = standardwert;
    }

    //Getter- und Setter-Methoden
    public Long getEmpfehlungsparameter_id(){
        return empfehlungsparameter_id;
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


}
