package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

/**
 * Buecher, die im Algorithmus beruecksichtigt werden
 * @param buch
 * @param klasse
 *
 * @author Niklas Prell
 */

public record Kandidat(BuchInfo buch, Kandidatenklasse klasse, double empfehlungScore) {

}
