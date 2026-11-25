package com.softwareprojekt.gruppe8.tagebuch.datatransfer;

import java.time.LocalDate;
import java.util.Map;

// Statistik einer täglichen Lesechallenge
public record TaeglicheChallengeStatistik(

// Vorgegebene Seitenzahl pro Tag
        int zielSeitenProTag,

// Anzahl der bisher berücksichtigten Tage
        long vergangeneTage,

// Anzahl der Tage mit erreichtem Leseziel
        long erfolgreicheTage,

// Gelesene Seiten pro Datum
        Map<LocalDate, Integer> seitenProTag

) {

    public TaeglicheChallengeStatistik {
        seitenProTag = Map.copyOf(seitenProTag);
    }

    // Fortschritt in Prozent
    public double fortschrittProzent() {

        if (vergangeneTage == 0) {
            return 0.0;
        }

        return erfolgreicheTage * 100.0 / vergangeneTage;
    }

    // Prüft, ob das tägliche Leseziel erreicht wurde
    public boolean tageszielErreicht(LocalDate datum) {

        return seitenProTag.getOrDefault(datum, 0)
                >= zielSeitenProTag;
    }
}
