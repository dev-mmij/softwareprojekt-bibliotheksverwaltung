package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import com.softwareprojekt.gruppe8.empfehlung.domain.EmpfehlungsparameterDefinition;
import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InteressenprofilTest {
    //Autorentestdaten
    final Long jkRowling = 10L, king = 11L, collins = 12L, tolkien = 13L;

    //Buechertestdaten
    final Long esId = 100L, hobbitId = 101L, hungerGamesId = 102L, harryPotterId = 104L;

    //Genretestdaten
    final Long fantasy = 1L, krimi = 2L, abenteuer = 3L, horror = 4L, thriller = 5L, dystopie = 6L;

    @Test
    void ergibtErwarteteGewichte() {
        //Daten vorbereiten
        BuchInfo hobbit = new BuchInfo(hobbitId, "Der Hobbit", Map.of(fantasy, "Fantasy", abenteuer, "Abenteuer"),
                Map.of(tolkien, "Tolkien"));
        BuchInfo es = new BuchInfo(esId, "ES", Map.of(horror, "Horror"), Map.of(king, "King"));
        BuchInfo hungerGames = new BuchInfo(hungerGamesId, "Die Tribute von Panem", Map.of(fantasy, "Fantasy", dystopie, "Dystopie"),
                Map.of(collins, "Collins"));
        BuchInfo harryPotter = new BuchInfo(harryPotterId, "Harry Potter", Map.of(fantasy, "Fantasy"), Map.of(jkRowling, "J.K Rowling"));

        //Vorherige Bewertungen
        List<VerlaufEintrag> verlaufEintraege = List.of(
                new VerlaufEintrag(hobbitId, 1, Feedback.LIKE),
                new VerlaufEintrag(esId, 2, Feedback.DISLIKE));

        AlgorithmusInput input = new AlgorithmusInput(
                List.of(hobbit, es, hungerGames, harryPotter), verlaufEintraege, Set.of(fantasy, dystopie),
                Set.of(collins, tolkien), Set.of(), 3);

        Map<EmpfehlungsparameterDefinition, Double> parameterWerte = Map.of(
                EmpfehlungsparameterDefinition.STARTWERT_LIEBLINGSGENRE, 0.5,
                EmpfehlungsparameterDefinition.BONUS_GEFOLGTER_AUTOR, 0.5,
                EmpfehlungsparameterDefinition.DAUMEN_HOCH_GENRE, 0.2,
                EmpfehlungsparameterDefinition.DAUMEN_RUNTER_GENRE, 0.2,
                EmpfehlungsparameterDefinition.DAUMEN_HOCH_AUTOR, 0.2,
                EmpfehlungsparameterDefinition.DAUMEN_RUNTER_AUTOR, 0.2,
                EmpfehlungsparameterDefinition.GEWICHT_MAX, 1.0,
                EmpfehlungsparameterDefinition.GEWICHT_MIN, -1.0);

        //Interessenprofil bilden
        Interessenprofil profil = new Interessenprofil(input, new Verlauf(verlaufEintraege), parameterWerte);

        //Testen (Erwarteter Wert, berechneter Wert)
        assertEquals(0.7, profil.getGenreGewicht(fantasy));
        assertEquals(0.2, profil.getGenreGewicht(abenteuer));
        assertEquals(-0.2, profil.getGenreGewicht(horror));
        assertEquals(0.5, profil.getGenreGewicht(dystopie));
        assertEquals(0.0, profil.getGenreGewicht(krimi));
        assertEquals(0.0, profil.getGenreGewicht(thriller));
        assertEquals(-0.2, profil.getAutorGewicht(king));
        assertEquals(0.5, profil.getAutorGewicht(collins));
        assertEquals(0.7, profil.getAutorGewicht(tolkien));
        assertEquals(0.0, profil.getAutorGewicht(jkRowling));
    }

}