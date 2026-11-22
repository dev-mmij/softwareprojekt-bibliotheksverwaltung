package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit-Test fuer den Verlauf zum Speichern der neuesten Empfehlung
 *
 * @author Niklas Prell
 */

class VerlaufTest {
    private static final Long HOBBIT = 100L;

    @Test
    void neuesteEmpfehlungWirdBetrachtet() {
        Verlauf verlauf = new Verlauf(List.of(
                new VerlaufEintrag(HOBBIT, 1, Feedback.LIKE),
                new VerlaufEintrag(HOBBIT, 5, Feedback.DISLIKE)
        ));
        assertTrue(verlauf.wurdeEmpfohlen(HOBBIT));
        assertTrue(verlauf.hatFeedback(HOBBIT, Feedback.DISLIKE));
        assertFalse(verlauf.hatFeedback(HOBBIT, Feedback.LIKE));
        assertFalse(verlauf.hatFeedback(HOBBIT, Feedback.NICHT_BEWERTET));
    }

    @Test
    void neuesteEmpfehlungReihenfolgeInDerListeEgal() {
        Verlauf verlauf = new Verlauf(List.of(
                new VerlaufEintrag(HOBBIT, 5, Feedback.DISLIKE),
                new VerlaufEintrag(HOBBIT, 1, Feedback.LIKE)
        ));
        assertTrue(verlauf.wurdeEmpfohlen(HOBBIT));
        assertTrue(verlauf.hatFeedback(HOBBIT, Feedback.DISLIKE));
        assertFalse(verlauf.hatFeedback(HOBBIT, Feedback.LIKE));
        assertFalse(verlauf.hatFeedback(HOBBIT, Feedback.NICHT_BEWERTET));
    }

    @Test
    void liefertFalseBeiJedemFeedbackWennBuchNichtImVerlauf() {
        Verlauf verlauf = new Verlauf(List.of());
        assertFalse(verlauf.wurdeEmpfohlen(HOBBIT));
        assertFalse(verlauf.hatFeedback(HOBBIT, Feedback.DISLIKE));
        assertFalse(verlauf.hatFeedback(HOBBIT, Feedback.LIKE));
        assertFalse(verlauf.hatFeedback(HOBBIT, Feedback.NICHT_BEWERTET));
    }

    @Test
    void wurdeKuerzlichEmpfohlenWirdErkannt() {
        Verlauf verlauf = new Verlauf(List.of(
                new VerlaufEintrag(HOBBIT, 5, Feedback.DISLIKE)
        ));
        assertTrue(verlauf.wurdeEmpfohlen(HOBBIT));
        assertTrue(verlauf.wurdeKuerzlichEmpfohlen(HOBBIT, 24, 20));
        assertFalse(verlauf.wurdeKuerzlichEmpfohlen(HOBBIT, 25, 20));
        assertFalse(verlauf.wurdeKuerzlichEmpfohlen(HOBBIT, 26, 20));
    }
}
