package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import com.softwareprojekt.gruppe8.empfehlung.domain.EmpfehlungsparameterDefinition;
import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests fuer das Interessenprofil zur Berechnung der Gewichte aus Genres und Autoren sowie vorherigen
 * Empfehlungen
 *
 * @author Niklas Prell
 */
class InteressenprofilTest {
    //Autorentestdaten
    private static final Long JK_ROWLING = 10L, KING = 11L, COLLINS = 12L, TOLKIEN = 13L, MCFADDEN = 14L;

    //Buechertestdaten
    private static final Long ES_ID = 100L, HOBBIT_ID = 101L, HUNGER_GAMES_ID = 102L, HARRY_POTTER_ID = 104L, WENN_SIE_WUESSTE_ID = 105L, LEERES_BUCH_ID = 106L;

    //Genretestdaten
    private static final Long FANTASY = 1L, KRIMI = 2L, ABENTEUER = 3L, HORROR = 4L, THRILLER = 5L, DYSTOPIE = 6L;

    //BuchInfos
    private BuchInfo hobbit;
    private BuchInfo es;
    private BuchInfo hungerGames;
    private BuchInfo harryPotter;
    private BuchInfo wennSieWuesste;
    private BuchInfo leeresBuch;

    //Interessensprofil
    private Interessenprofil profil;
    private Interessenprofil begrenztesProfil;

    @BeforeEach
    void setUp() {
        //Daten vorbereiten
        hobbit = new BuchInfo(HOBBIT_ID, "Der Hobbit", Map.of(FANTASY, "Fantasy", ABENTEUER, "Abenteuer"),
                Map.of(TOLKIEN, "Tolkien"));
        es = new BuchInfo(ES_ID, "ES", Map.of(HORROR, "Horror"), Map.of(KING, "King"));
        hungerGames = new BuchInfo(HUNGER_GAMES_ID, "Die Tribute von Panem", Map.of(FANTASY, "Fantasy", DYSTOPIE, "Dystopie"),
                Map.of(COLLINS, "Collins"));
        harryPotter = new BuchInfo(HARRY_POTTER_ID, "Harry Potter", Map.of(FANTASY, "Fantasy"), Map.of(JK_ROWLING, "J.K Rowling"));
        wennSieWuesste = new BuchInfo(WENN_SIE_WUESSTE_ID, "Wenn Sie wuesste", Map.of(THRILLER, "Thriller"), Map.of(MCFADDEN, "McFadden"));
        leeresBuch = new BuchInfo(LEERES_BUCH_ID, "", Map.of(), Map.of());

        //Vorherige Bewertungen
        List<VerlaufEintrag> verlaufEintraege = List.of(
                new VerlaufEintrag(HOBBIT_ID, 1, Feedback.LIKE),
                new VerlaufEintrag(ES_ID, 2, Feedback.DISLIKE));

        AlgorithmusInput input = new AlgorithmusInput(
                List.of(hobbit, es, hungerGames, harryPotter), verlaufEintraege, Set.of(FANTASY, DYSTOPIE),
                Set.of(COLLINS, TOLKIEN), Set.of(), 3);

        Map<EmpfehlungsparameterDefinition, Double> parameterWerte = new HashMap<>(Map.of(
                EmpfehlungsparameterDefinition.STARTWERT_LIEBLINGSGENRE, 0.5,
                EmpfehlungsparameterDefinition.BONUS_GEFOLGTER_AUTOR, 0.5,
                EmpfehlungsparameterDefinition.DAUMEN_HOCH_GENRE, 0.2,
                EmpfehlungsparameterDefinition.DAUMEN_RUNTER_GENRE, 0.2,
                EmpfehlungsparameterDefinition.DAUMEN_HOCH_AUTOR, 0.2,
                EmpfehlungsparameterDefinition.DAUMEN_RUNTER_AUTOR, 0.2,
                EmpfehlungsparameterDefinition.GEWICHT_MAX, 1.0,
                EmpfehlungsparameterDefinition.GEWICHT_MIN, -1.0));

        //Interessenprofil bilden
        profil = new Interessenprofil(input, new Verlauf(verlaufEintraege), parameterWerte);

        //Begrenztes Interessenprofil bilden
        parameterWerte.put(EmpfehlungsparameterDefinition.GEWICHT_MAX, 0.5);
        begrenztesProfil = new Interessenprofil(input, new Verlauf(verlaufEintraege), parameterWerte);
    }
    @Test
    void berechnetGewichteAusLieblingsgenresFeedbackUndGefolgtenAutoren() {
        assertEquals(0.7, profil.getGenreGewicht(FANTASY));
        assertEquals(0.2, profil.getGenreGewicht(ABENTEUER));
        assertEquals(-0.2, profil.getGenreGewicht(HORROR));
        assertEquals(0.5, profil.getGenreGewicht(DYSTOPIE));
        assertEquals(0.0, profil.getGenreGewicht(KRIMI));
        assertEquals(0.0, profil.getGenreGewicht(THRILLER));
        assertEquals(-0.2, profil.getAutorGewicht(KING));
        assertEquals(0.5, profil.getAutorGewicht(COLLINS));
        assertEquals(0.7, profil.getAutorGewicht(TOLKIEN));
        assertEquals(0.0, profil.getAutorGewicht(JK_ROWLING));
    }

    @Test
    void erkenntBuchMitNurUnbekanntenGenresUndAutoren() {
        assertTrue(profil.istUnbekannt(wennSieWuesste));
        assertFalse(profil.istUnbekannt(hobbit));
    }

    /*double fuehrt zu Berechnungsungenauigkeiten, die mit dem dritten Parameter abgefangen werden. Da der spaetere Algorithmus
    nur die Reihenfolge betrachtet, sind diese dort nicht kritisch */
    @Test
    void berechnetGenreDurchschnittBeiBuchMitMehrerenBewertungen() {
        assertEquals(0.45, profil.getGenreDurchschnitt(hobbit), 1e-10);
    }

    @Test
    void liefertDurchschnittVonNullBeiBuchMitKeinerBewertung() {
        assertEquals(0.0, profil.getGenreDurchschnitt(wennSieWuesste));
    }

    @Test
    void liefertDurchschnittVonNullBeiBuchOhneGenreUndOhneAutor() {
        assertEquals(0.0, profil.getGenreDurchschnitt(leeresBuch));
        assertEquals(0.0, profil.getAutorDurchschnitt(leeresBuch));
    }

    @Test
    void speichertObereGewichtGrenzeFuerGenreOderAutorWennDieGrenzeUeberschrittenWird() {
        assertEquals(0.5, begrenztesProfil.getGenreGewicht(FANTASY));
        assertEquals(0.5, begrenztesProfil.getAutorGewicht(TOLKIEN));
    }

    @Test
    void speichertNormalesGewichtAuchBeiStaerkererBegrenzung() {
        assertEquals(0.2, begrenztesProfil.getGenreGewicht(ABENTEUER));
        assertEquals(0.5, begrenztesProfil.getAutorGewicht(COLLINS));
    }
}