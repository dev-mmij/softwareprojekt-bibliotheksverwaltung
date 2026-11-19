package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

/**
 * Ergebnis des Algorithmus, den der Service verwendet, um daraus Empfehlungen zu generieren
 * @param buchId
 * @param position
 * @param score
 * @param grund
 * @param istExploration
 * @author Niklas Prell
 */

public record Vorschlag(Long buchId, int position, double score, String grund, boolean istExploration) {
}
