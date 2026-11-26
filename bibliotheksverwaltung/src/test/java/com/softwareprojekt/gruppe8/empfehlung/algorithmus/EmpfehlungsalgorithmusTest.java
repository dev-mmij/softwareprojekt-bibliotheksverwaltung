package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import com.softwareprojekt.gruppe8.empfehlung.domain.EmpfehlungsparameterDefinition;
import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests fuer den Empfehlungsalgorithmus
 *
 * @author Niklas Prell
 */
class EmpfehlungsalgorithmusTest {
    //Autorentestdaten
    private static final Long JK_ROWLING = 10L, KING = 11L, COLLINS = 12L, TOLKIEN = 13L, MCFADDEN = 14L;

    //Buechertestdaten
    private static final Long ES_ID = 100L, HOBBIT_ID = 101L, HUNGER_GAMES_ID = 102L, HARRY_POTTER_ID = 104L, WENN_SIE_WUESSTE_ID = 105L,
                            FRIEDHOF_DER_KUSCHELTIERE_ID = 106L, SHINING_ID = 107L, CARRIE_ID = 108L, REVIVAL_ID = 109L, CUJO_ID = 110L,
                            CHRISTINE_ID = 111L;

    //Genretestdaten
    private static final Long FANTASY = 1L, KRIMI = 2L, ABENTEUER = 3L, HORROR = 4L, THRILLER = 5L, DYSTOPIE = 6L;

    //BuchInfos
    private BuchInfo hobbit;
    private BuchInfo es;
    private BuchInfo hungerGames;
    private BuchInfo harryPotter;
    private BuchInfo wennSieWuesste;
    private BuchInfo friedhofDerKuscheltiere;
    private BuchInfo shining;
    private BuchInfo carrie;
    private BuchInfo revival;
    private BuchInfo cujo;
    private BuchInfo christine;


    private Empfehlungsalgorithmus algorithmus;

    private Map<EmpfehlungsparameterDefinition, Double> parameter;

    @BeforeEach
    void setUp() {
        algorithmus = new Empfehlungsalgorithmus(new Random());

        //Aenderbare Map der Parameter, damit die Tests unterschiedliche Werte einsetzen koennen
        parameter = new HashMap<>();
        parameter.put(EmpfehlungsparameterDefinition.STARTWERT_LIEBLINGSGENRE, 0.5);
        parameter.put(EmpfehlungsparameterDefinition.DAUMEN_HOCH_GENRE, 0.1);
        parameter.put(EmpfehlungsparameterDefinition.DAUMEN_HOCH_AUTOR, 0.15);
        parameter.put(EmpfehlungsparameterDefinition.DAUMEN_RUNTER_GENRE, 0.05);
        parameter.put(EmpfehlungsparameterDefinition.DAUMEN_RUNTER_AUTOR, 0.05);
        parameter.put(EmpfehlungsparameterDefinition.BONUS_GEFOLGTER_AUTOR, 0.25);
        parameter.put(EmpfehlungsparameterDefinition.GEWICHT_MIN, -1.0);
        parameter.put(EmpfehlungsparameterDefinition.GEWICHT_MAX, 1.0);
        parameter.put(EmpfehlungsparameterDefinition.ANTEIL_AUTOR, 0.3);
        parameter.put(EmpfehlungsparameterDefinition.ZUFALL_MAX, 0.0);
        parameter.put(EmpfehlungsparameterDefinition.ENTDECKUNGSPLAETZE, 0.0);
        parameter.put(EmpfehlungsparameterDefinition.MINDESTABSTAND_WIEDERHOLUNG, 20.0);

        hobbit = new BuchInfo(HOBBIT_ID, "Der Hobbit", Map.of(FANTASY, "Fantasy", ABENTEUER, "Abenteuer"),
                Map.of(TOLKIEN, "Tolkien"));
        es = new BuchInfo(ES_ID, "ES", Map.of(HORROR, "Horror"), Map.of(KING, "King"));
        hungerGames = new BuchInfo(HUNGER_GAMES_ID, "Die Tribute von Panem", Map.of(FANTASY, "Fantasy", DYSTOPIE, "Dystopie"),
                Map.of(COLLINS, "Collins"));
        harryPotter = new BuchInfo(HARRY_POTTER_ID, "Harry Potter", Map.of(FANTASY, "Fantasy"), Map.of(JK_ROWLING, "J.K Rowling"));
        wennSieWuesste = new BuchInfo(WENN_SIE_WUESSTE_ID, "Wenn Sie wuesste", Map.of(THRILLER, "Thriller"), Map.of(MCFADDEN, "McFadden"));
        friedhofDerKuscheltiere = new BuchInfo(FRIEDHOF_DER_KUSCHELTIERE_ID, "Friedhof der Kuscheltiere", Map.of(HORROR, "Horror"), Map.of(KING, "King"));
        shining = new BuchInfo(SHINING_ID, "Shining", Map.of(HORROR, "Horror"), Map.of(KING, "King"));
        carrie = new BuchInfo(CARRIE_ID, "Carrie", Map.of(HORROR, "Horror"), Map.of(KING, "King"));
        revival = new BuchInfo(REVIVAL_ID, "Revival", Map.of(HORROR, "Horror"), Map.of(KING, "King"));
        cujo = new BuchInfo(CUJO_ID, "Cujo", Map.of(HORROR, "Horror"), Map.of(KING, "King"));
        christine = new BuchInfo(CHRISTINE_ID, "Christine", Map.of(HORROR, "Horror"), Map.of(KING, "King"));
    }

    private List<Long> getVorschlaegeIds(List<Vorschlag> vorschlaege){
        return vorschlaege.stream().map(Vorschlag::buchId).toList();
    }

    @Test
    void schliesstNichtGemochteUndAusgeschlosseneBuecherAus() {
        AlgorithmusInput input = new AlgorithmusInput(
                List.of(hobbit, es, hungerGames, harryPotter, wennSieWuesste),
                List.of(new VerlaufEintrag(HOBBIT_ID, 1, Feedback.DISLIKE)),
                Set.of(),
                Set.of(),
                Set.of(ES_ID), //Ausgeschlossenes Buch
                11);

        List<Long> vorschlaegeIds = getVorschlaegeIds(algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter));

        assertEquals(Set.of(HUNGER_GAMES_ID, HARRY_POTTER_ID, WENN_SIE_WUESSTE_ID), new HashSet<>(vorschlaegeIds));
    }

    @Test
    void sortiertNachKlasseVorScore() {
        AlgorithmusInput input = new AlgorithmusInput(
                List.of(harryPotter, es, wennSieWuesste),
                List.of(new VerlaufEintrag(HARRY_POTTER_ID, 1, Feedback.LIKE),
                        new VerlaufEintrag(ES_ID, 2, Feedback.NICHT_BEWERTET)),
                Set.of(),
                Set.of(),
                Set.of(),
                11);

        List<Long> vorgeschlaegeIds = getVorschlaegeIds(algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter));

        assertEquals(List.of(WENN_SIE_WUESSTE_ID, ES_ID, HARRY_POTTER_ID), vorgeschlaegeIds);
    }

    @Test
    void sortiertInnerhalbEinerKlasseNachScore() {
        AlgorithmusInput input = new AlgorithmusInput(
                List.of(es, hungerGames),
                List.of(),
                Set.of(DYSTOPIE),
                Set.of(),
                Set.of(),
                1);

        List<Long> vorschlaegeIds = getVorschlaegeIds(algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter));

        assertEquals(List.of(HUNGER_GAMES_ID, ES_ID),  vorschlaegeIds);
    }

    @Test
    void vergibtPositionenAbNaechsterPosition() {
        AlgorithmusInput input = new AlgorithmusInput(
                List.of(hobbit, es, hungerGames, harryPotter, wennSieWuesste),
                List.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                1);

        List<Integer> positionen = algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter)
                .stream().map(Vorschlag::position).toList();

        assertEquals(List.of(1, 2, 3, 4, 5),  positionen);
    }

    @Test
    void setztExplorationaufBerechnetePlaetze() {
        AlgorithmusInput input = new AlgorithmusInput(
                List.of(hobbit, es, hungerGames, harryPotter, wennSieWuesste, friedhofDerKuscheltiere, shining, carrie,
                        revival, cujo, christine),
                List.of(new VerlaufEintrag(HOBBIT_ID, 1, Feedback.LIKE)),
                Set.of(),
                Set.of(),
                Set.of(),
                11);

        parameter.put(EmpfehlungsparameterDefinition.ENTDECKUNGSPLAETZE, 2.0);

        List<Integer> explorationspositionen = algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter)
                .stream().filter(Vorschlag::istExploration).map(Vorschlag::position).toList();

        assertEquals(List.of(14, 17), explorationspositionen);
    }

    @Test
    void fuelltExplorationspositionenNormalOhneUnbekannteBuecher() {
        AlgorithmusInput input = new AlgorithmusInput(
                List.of(hobbit, es, hungerGames, harryPotter, wennSieWuesste, friedhofDerKuscheltiere, shining, carrie,
                        revival, cujo, christine),
                List.of(new VerlaufEintrag(HOBBIT_ID, 1, Feedback.LIKE),
                        new VerlaufEintrag(ES_ID, 2, Feedback.LIKE),
                        new VerlaufEintrag(HUNGER_GAMES_ID, 3, Feedback.LIKE),
                        new VerlaufEintrag(HARRY_POTTER_ID, 4, Feedback.LIKE),
                        new VerlaufEintrag(WENN_SIE_WUESSTE_ID, 5, Feedback.LIKE)),
                Set.of(),
                Set.of(),
                Set.of(),
                11);

        parameter.put(EmpfehlungsparameterDefinition.ENTDECKUNGSPLAETZE, 2.0);

        int anzahlExploration = algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter)
                .stream().filter(Vorschlag::istExploration).toList().size();

        assertEquals(0, anzahlExploration);
    }

    @Test
    void sperreKuerzlichEmpfohleneBuecher() {
        AlgorithmusInput input = new AlgorithmusInput(
                List.of(hobbit, es, hungerGames, harryPotter, wennSieWuesste, friedhofDerKuscheltiere, shining, carrie,
                        revival, cujo, christine),
                List.of(new VerlaufEintrag(HOBBIT_ID, 1, Feedback.LIKE)),
                Set.of(),
                Set.of(),
                Set.of(),
                11);



        List<Long> vorschlaege = algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter)
                .stream().map(Vorschlag::buchId).toList();

        assertFalse(vorschlaege.contains(HOBBIT_ID));
    }

    @Test
    void hebtSperreAufWennZuWenigeKandidaten() {
        AlgorithmusInput input = new AlgorithmusInput(
                List.of(hobbit, es, hungerGames, harryPotter, wennSieWuesste, friedhofDerKuscheltiere, shining, carrie,
                        revival, cujo),
                List.of(new VerlaufEintrag(HOBBIT_ID, 1, Feedback.LIKE)),
                Set.of(),
                Set.of(),
                Set.of(),
                11);



        List<Long> vorschlaege = algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter)
                .stream().map(Vorschlag::buchId).toList();

        assertTrue(vorschlaege.contains(HOBBIT_ID));
    }

    @Test
    void liefertHoechstensZehnKartenProEmpfehlungsrunde() {
        AlgorithmusInput input = new AlgorithmusInput(
                List.of(hobbit, es, hungerGames, harryPotter, wennSieWuesste, friedhofDerKuscheltiere, shining, carrie,
                        revival, cujo, christine),
                List.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                1);



        List<Vorschlag> vorschlaege = algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter);

        assertEquals(10, vorschlaege.size());
    }

    @Test
    void liefertLeereRundeWennAlleAusgeschlossen() {
        AlgorithmusInput input = new AlgorithmusInput(
                List.of(hobbit, es, hungerGames, harryPotter, wennSieWuesste, friedhofDerKuscheltiere, shining, carrie,
                        revival, cujo, christine),
                List.of(),
                Set.of(),
                Set.of(),
                Set.of(HOBBIT_ID, ES_ID, HUNGER_GAMES_ID, HARRY_POTTER_ID, WENN_SIE_WUESSTE_ID, FRIEDHOF_DER_KUSCHELTIERE_ID,
                        SHINING_ID, CARRIE_ID, REVIVAL_ID, CUJO_ID, CHRISTINE_ID),
                1);



        List<Vorschlag> vorschlaege = algorithmus.generiereNaechsteEmpfehlungsrunde(input, parameter);

        assertEquals(0, vorschlaege.size());
    }
}