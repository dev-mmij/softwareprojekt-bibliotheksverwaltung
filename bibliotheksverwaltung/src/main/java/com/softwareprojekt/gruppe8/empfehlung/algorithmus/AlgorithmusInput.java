package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import java.util.List;
import java.util.Set;

/**
 * Kapselung aller Werte die der Algorithmus zur Berechnung einer neuen Empfehlung benoetigt.
 * @param alleBuecher
 * @param verlauf
 * @param lieblingsgenreIds
 * @param gefolgteAutorIds
 * @param ausgeschlosseneBuchIds
 * @param naechstePosition
 * @author Niklas Prell
 */

public record AlgorithmusInput(List<BuchInfo> alleBuecher, List<VerlaufEintrag> verlauf, Set<Long> lieblingsgenreIds,
                               Set<Long> gefolgteAutorIds, Set<Long> ausgeschlosseneBuchIds, int naechstePosition) {
}
