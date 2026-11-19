package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;

/**
 * Kapselung der Daten bisheriger Empfehlungen zur Berechnung zukuenftiger Empfehlungen
 * @param buchId
 * @param position
 * @param feedback
 * @author Niklas Prell
 */
public record VerlaufEintrag(Long buchId, int position, Feedback feedback) {
    public boolean istNeuerAls(VerlaufEintrag andererEintrag){
        return position > andererEintrag.position;
    }
}