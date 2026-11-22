package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlungsparameter;
import com.softwareprojekt.gruppe8.empfehlung.domain.EmpfehlungsparameterDefinition;
import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;

import java.util.*;
import java.util.stream.Collectors;

public class Empfehlungsalgorithmus {
    private static final int KARTEN_PRO_RUNDE = 10;
    private Random zufall;

    public Empfehlungsalgorithmus(Random zufall) {
        this.zufall = zufall;
    }

    public List<Vorschlag> generiereNaechsteEmpfehlungsrunde(AlgorithmusInput input, Map<EmpfehlungsparameterDefinition, Double> parameter) {
        Verlauf verlauf = new Verlauf(input.verlauf());
        Interessenprofil profil = new Interessenprofil(input, verlauf, parameter);
        List<Kandidat> kandidaten = bestimmeKandidaten(input, verlauf, parameter, profil);
        return generiereVorschlaege(kandidaten, profil, parameter, input.naechstePosition());
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
                if (!verlauf.wurdeKuerzlichEmpfohlen(buch.buchId(), input.naechstePosition(), mindestabstand)) {
                    kandidaten.add(kandidat);
                } else {
                    eventuelleKandidaten.add(kandidat);
                }
            }
        }
        if (kandidaten.size() < KARTEN_PRO_RUNDE) {
            kandidaten.addAll(eventuelleKandidaten);
        }
        return kandidaten;
    }

    /**
     * Ordnet ein Buch anhand seines Verlaufs einer Kandidatenklasse zu.
     */
    private Kandidatenklasse bestimmeKlasse(BuchInfo buch, Verlauf verlauf) {
        if (!verlauf.wurdeEmpfohlen(buch.buchId())) {
            return Kandidatenklasse.NEU;
        } else if (verlauf.hatFeedback(buch.buchId(), Feedback.LIKE)) {
            return Kandidatenklasse.GELIKED;
        } else {
            return Kandidatenklasse.UEBERSPRUNGEN;
        }
    }

    /**
     * Berechnung eines Zahlenwerts wie gut ein Buch zum erstellten Interessensprofil passt
     */
    private double berechneEmpfehlungScore(BuchInfo buch, Interessenprofil profil, Map<EmpfehlungsparameterDefinition, Double> parameter) {
        double anteilAutor = parameter.get(EmpfehlungsparameterDefinition.ANTEIL_AUTOR);
        double maximalerZufall = parameter.get(EmpfehlungsparameterDefinition.ZUFALL_MAX);
        double zufallsWert = zufall.nextDouble() * maximalerZufall; // Verhindert Exception, wenn der Wert fuer den maximalen Zufall 0 ist
        return profil.getGenreDurchschnitt(buch) + anteilAutor * profil.getAutorDurchschnitt(buch) + zufallsWert;
    }

    /**
     * Stellt aus den gefundenen Kandidaten eine Runde zusammen. Normale Positionen bekommen den besten verbleibenden
     * Kandidaten, Explorationsplaetze einen mit unbekannten Genres und Autoren. Liegen keine mit unbekannten Genres oder
     * Autoren mehr vor, wird stattdessen eine normale Empfehlung hinzugefuegt.
     *
     * @param kandidaten       Liste der Kandidaten
     * @param profil           Generiertes Interessenprofil
     * @param parameter        Aktuell gueltige Parametereinstellungen
     * @param naechstePosition Position der naechsten Empfehlung
     * @return Vorschlaege fuer eine neue Empfehlungsrunde
     */
    private List<Vorschlag> generiereVorschlaege(List<Kandidat> kandidaten, Interessenprofil profil, Map<EmpfehlungsparameterDefinition, Double> parameter, int naechstePosition) {
        List<Vorschlag> vorschlaege = new ArrayList<>();
        List<Kandidat> sortierteKandidaten = kandidaten.stream().sorted(Comparator.comparing(Kandidat::klasse)
                .thenComparing(Kandidat::empfehlungScore, Comparator.reverseOrder())).collect(Collectors.toList());
        List<Integer> explorationspositionen = berechneExplorationspositionen(parameter.get(EmpfehlungsparameterDefinition.ENTDECKUNGSPLAETZE).intValue());

        for (int empfehlung = 1; empfehlung <= KARTEN_PRO_RUNDE && !sortierteKandidaten.isEmpty(); empfehlung++) {
            Kandidat kandidat = null;
            if (explorationspositionen.contains(empfehlung)) {
                kandidat = liefereErstenUnbekanntenKandidat(sortierteKandidaten, profil);
            }
            boolean istExploration = kandidat != null;
            if (kandidat == null) {
                kandidat = sortierteKandidaten.removeFirst();
            }
            Vorschlag vorschlag = new Vorschlag(kandidat.buch().buchId(), naechstePosition + empfehlung - 1,
                    kandidat.empfehlungScore(), "", istExploration);
            vorschlaege.add(vorschlag);
        }
        return vorschlaege;
    }

    private List<Integer> berechneExplorationspositionen(int anzahl) {
        List<Integer> positionen = new ArrayList<>();
        for (int i = 1; i <= anzahl; i++) {
            int position = (int) Math.ceil(i * (double) KARTEN_PRO_RUNDE / (anzahl + 1));
            positionen.add(position);
        }
        return positionen;
    }

    private Kandidat liefereErstenUnbekanntenKandidat(List<Kandidat> kandidaten, Interessenprofil profil) {
        for (Kandidat kandidat : kandidaten) {
            if (profil.istUnbekannt(kandidat.buch())) {
                kandidaten.remove(kandidat);
                return kandidat;
            }
        }
        return null;
    }
}
