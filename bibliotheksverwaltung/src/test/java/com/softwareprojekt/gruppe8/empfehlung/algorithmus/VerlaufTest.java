package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class VerlaufTest {
    final Long hobbit = 100L;

    @Test
    void neuesteEmpfehlungWirdBetrachtet() {
        Verlauf verlauf = new Verlauf(List.of(
                new VerlaufEintrag(hobbit, 1, Feedback.LIKE),
                new VerlaufEintrag(hobbit, 5, Feedback.DISLIKE)
        ));

        assertTrue(verlauf.hatFeedback(hobbit, Feedback.DISLIKE));
        assertFalse(verlauf.hatFeedback(hobbit, Feedback.LIKE));
        assertFalse(verlauf.hatFeedback(hobbit, Feedback.NICHT_BEWERTET));
    }

    @Test
    void neuesteEmpfehlungWirdBetrachtetBasierendAnhandDerPosition() {
        Verlauf verlauf = new Verlauf(List.of(
                new VerlaufEintrag(hobbit, 5, Feedback.DISLIKE),
                new VerlaufEintrag(hobbit, 1, Feedback.LIKE)
        ));

        assertTrue(verlauf.hatFeedback(hobbit, Feedback.DISLIKE));
        assertFalse(verlauf.hatFeedback(hobbit, Feedback.LIKE));
        assertFalse(verlauf.hatFeedback(hobbit, Feedback.NICHT_BEWERTET));
    }

    @Test
    void liefertFalseBeiJedemFeedbackWennBuchNichtImVerlauf() {
        Verlauf verlauf = new Verlauf(List.of());
        assertFalse(verlauf.hatFeedback(hobbit, Feedback.DISLIKE));
        assertFalse(verlauf.hatFeedback(hobbit, Feedback.LIKE));
        assertFalse(verlauf.hatFeedback(hobbit, Feedback.NICHT_BEWERTET));
    }

    @Test
    void wurdeKuerzlichEmpfohlenWirdErkannt() {
        Verlauf verlauf = new Verlauf(List.of(
                new VerlaufEintrag(hobbit, 5, Feedback.DISLIKE)
        ));

        assertTrue(verlauf.wurdeKuerzlichEmpfohlen(hobbit, 24, 20));
        assertFalse(verlauf.wurdeKuerzlichEmpfohlen(hobbit, 25, 20));
        assertFalse(verlauf.wurdeKuerzlichEmpfohlen(hobbit, 26, 20));
    }
}
