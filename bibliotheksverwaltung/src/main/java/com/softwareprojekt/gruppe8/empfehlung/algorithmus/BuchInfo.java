package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import java.util.Map;

/**
 * Kapselung der Daten eines Buches, die der Empfehlungsalgorithmus braucht. Damit wird der Algorithmus zeitgleich von
 * der Datenbank geloest.
 * @author Niklas Prell
 */

public record BuchInfo(Long buchId, String titel, Map<Long, String> genres, Map<Long, String> autoren) {
}
