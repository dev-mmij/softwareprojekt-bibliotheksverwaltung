package com.softwareprojekt.gruppe8.tagebuch.datatransfer;

import java.util.List;

// Enthält die Statistikdaten für die ChallengeView.
// Die Werte werden später im ChallengeService berechnet.
public record ChallengeStatistik(

// Ziel der Lesechallenge
        Integer zielBuecher,

// Anzahl der vollständig gelesenen Bücher
        Integer geleseneBuecher,

// Anzahl der geplanten Bücher
        Integer geplanteBuecher,

// Anzahl der Bücher auf der Wunschliste
        Integer wunschliste,

// Monatliches Leseziel
        Integer monatsziel,

// Durchschnittlich gelesene Bücher pro Monat
        Double lesetempo,

// Anzahl gelesener Bücher je Monat (Januar bis Dezember)
        List<Integer> buecherProMonat

) {

    // Verhindert, dass die Monatsliste nachträglich
// über eine bestehende Listenreferenz verändert wird.
    public ChallengeStatistik {
        if (buecherProMonat != null) {
            if (buecherProMonat.size() != 12) {
                throw new IllegalArgumentException(
                        "Die Monatsstatistik muss 12 Werte enthalten."
                );
            }

            buecherProMonat = List.copyOf(buecherProMonat);
        }
    }

    // Erstellt eine Statistik ohne vorhandene Daten.
// null bedeutet: Daten sind noch nicht verfügbar.
    public static ChallengeStatistik ohneDaten() {
        return new ChallengeStatistik(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    // Berechnet den Fortschritt in Prozent.
// null bedeutet: Es ist kein Leseziel vorhanden.
    public Double fortschrittProzent() {

        if (zielBuecher == null || zielBuecher <= 0
                || geleseneBuecher == null) {
            return null;
        }

        return Math.min(
                100.0,
                geleseneBuecher * 100.0 / zielBuecher
        );
    }
}

