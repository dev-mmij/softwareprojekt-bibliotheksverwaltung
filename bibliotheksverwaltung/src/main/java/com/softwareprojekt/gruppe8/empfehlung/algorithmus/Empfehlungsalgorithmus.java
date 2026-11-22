package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import com.softwareprojekt.gruppe8.empfehlung.domain.EmpfehlungsparameterDefinition;
import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Empfehlungsalgorithmus {
    private static final int KARTEN_PRO_RUNDE = 10;
    private Random zufall;

    public Empfehlungsalgorithmus(Random zufall) {
        this.zufall = zufall;
    }

    /**
     * Bestimmt alle Buecher, die in die fuer die Empfehlungsgenerierung infrage kommen koennen.
     * Ausgeschlossene und nicht gemochte Buecher fallen weg. Kuerzlich empfohlene Buecher
     * kommen nur dazu, wenn sonst nicht genug Kandidaten für eine Runde da sind.
     *
     * @param input     alle Buecher und ausgeschlossenen Buch-IDs
     * @param verlauf   bisherige Empfehlungen mit Feedback
     * @param parameter aktuelle Werte der Empfehlungsparameter
     * @return Kandidaten mit ihrer Klasse, leer wenn keine Buecher mehr infrage kommen
     */
    private List<Kandidat> bestimmeKandidaten(AlgorithmusInput input, Verlauf verlauf, Map<EmpfehlungsparameterDefinition, Double> parameter, Interessenprofil profil) {
        List<Kandidat> kandidaten = new ArrayList<>();
        List<Kandidat> eventuelleKandidaten = new ArrayList<>();
        int mindestabstand = parameter.get(EmpfehlungsparameterDefinition.MINDESTABSTAND_WIEDERHOLUNG).intValue();
        for (BuchInfo buch : input.alleBuecher()) {
            if (!input.ausgeschlosseneBuchIds().contains(buch.buchId()) && !verlauf.hatFeedback(buch.buchId(), Feedback.DISLIKE)) {
                Kandidatenklasse klasse = bestimmeKlasse(buch, verlauf);
                Kandidat kandidat = new Kandidat(buch, klasse, berechneEmpfehlungScore(buch, profil, parameter));
                if(!verlauf.wurdeKuerzlichEmpfohlen(buch.buchId(), input.naechstePosition(), mindestabstand)) {
                    kandidaten.add(kandidat);
                }
                else {
                    eventuelleKandidaten.add(kandidat);
                }
            }
        }
        if (kandidaten.size() < KARTEN_PRO_RUNDE) {
            kandidaten.addAll(eventuelleKandidaten);
        }
        return kandidaten;
    }

    /** Ordnet ein Buch anhand seines Verlaufs einer Kandidatenklasse zu. */
    private Kandidatenklasse bestimmeKlasse(BuchInfo buch, Verlauf verlauf){
        if(!verlauf.wurdeEmpfohlen(buch.buchId())) {
            return Kandidatenklasse.NEU;
        }
        else if(verlauf.hatFeedback(buch.buchId(), Feedback.LIKE)) {
            return Kandidatenklasse.GELIKED;
        }
        else {
            return Kandidatenklasse.UEBERSPRUNGEN;
        }
    }

    /** Berechnung eines Zahlenwerts wie gut ein Buch zum erstellten Interessensprofil passt */
    private double berechneEmpfehlungScore(BuchInfo buch, Interessenprofil profil, Map<EmpfehlungsparameterDefinition, Double> parameter){
        double anteilAutor = parameter.get(EmpfehlungsparameterDefinition.ANTEIL_AUTOR);
        double maximalerZufall = parameter.get(EmpfehlungsparameterDefinition.ZUFALL_MAX);
        double zufallsWert = zufall.nextDouble() * maximalerZufall; // Verhindert Exception, wenn der Wert fuer den maximalen Zufall 0 ist
        return profil.getGenreDurchschnitt(buch) + anteilAutor * profil.getAutorDurchschnitt(buch) + zufallsWert;
    }
}
