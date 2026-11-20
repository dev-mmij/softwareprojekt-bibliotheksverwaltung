package com.softwareprojekt.gruppe8.empfehlung.algorithmus;

import com.softwareprojekt.gruppe8.empfehlung.domain.EmpfehlungsparameterDefinition;
import com.softwareprojekt.gruppe8.empfehlung.domain.Feedback;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Berechnung und Speicherung wie stark ein Benutzer einzelne Genres und Autoren mag.
 * Wird bei jeder neuen Empfehlungsrunde neu berechnet und nicht persistent gespeichert.
 *
 * @author Niklas Prell
 */
public class Interessenprofil {
    private final Map<Long, Double> genreGewichte = new HashMap<>();
    private final Map<Long, Double> autorGewichte = new HashMap<>();

    //Berechnung der Gewichte
    public Interessenprofil(AlgorithmusInput input, Verlauf verlauf, Map<EmpfehlungsparameterDefinition, Double> parameterWerte) {
        //Einbeziehen der Lieblingsgenres
        for (Long genreId : input.lieblingsgenreIds()) {
            summiereGewichte(genreGewichte, genreId, parameterWerte.get(EmpfehlungsparameterDefinition.STARTWERT_LIEBLINGSGENRE));
        }

        //Einbeziehen der vergebenen Feedbacks vorheriger Empfehlungen
        for (BuchInfo buch : input.alleBuecher()) {
            double genreWert;
            double autorWert;
            if (verlauf.hatFeedback(buch.buchId(), Feedback.LIKE)) {
                genreWert = parameterWerte.get(EmpfehlungsparameterDefinition.DAUMEN_HOCH_GENRE);
                autorWert = parameterWerte.get(EmpfehlungsparameterDefinition.DAUMEN_HOCH_AUTOR);
            } else if (verlauf.hatFeedback(buch.buchId(), Feedback.DISLIKE)) {
                genreWert = -(parameterWerte.get(EmpfehlungsparameterDefinition.DAUMEN_RUNTER_GENRE));
                autorWert = -(parameterWerte.get(EmpfehlungsparameterDefinition.DAUMEN_RUNTER_AUTOR));
            } else {
                continue; //Uebersprungene Buecher
            }
            for (Long genreId : buch.genres().keySet()) {
                summiereGewichte(genreGewichte, genreId, genreWert);
            }
            for (Long autorId : buch.autoren().keySet()) {
                summiereGewichte(autorGewichte, autorId, autorWert);
            }
        }

        //Gefolgte Autoren
        for (Long autorId : input.gefolgteAutorIds()) {
            summiereGewichte(autorGewichte, autorId, parameterWerte.get(EmpfehlungsparameterDefinition.BONUS_GEFOLGTER_AUTOR));
        }

        //Begrenzen der Gewichte
        genreGewichte.replaceAll((id, gewicht) -> begrenze(gewicht, parameterWerte.get(EmpfehlungsparameterDefinition.GEWICHT_MIN),
                parameterWerte.get(EmpfehlungsparameterDefinition.GEWICHT_MAX)));
        autorGewichte.replaceAll((id, gewicht) -> begrenze(gewicht, parameterWerte.get(EmpfehlungsparameterDefinition.GEWICHT_MIN),
                parameterWerte.get(EmpfehlungsparameterDefinition.GEWICHT_MAX)));
    }

    //Wenn das Genre noch nicht bewertet wurde, wird 0.0 geliefert
    public double getGenreGewicht(Long genreId) {
        return genreGewichte.getOrDefault(genreId, 0.0);
    }

    //Wenn der Autor noch nicht bewertet wurde, wird 0.0 geliefert
    public double getAutorGewicht(Long autorId) {
        return autorGewichte.getOrDefault(autorId, 0.0);
    }

    public double getGenreDurchschnitt(BuchInfo buch) {
        double genreSumme = 0.0;
        Set<Long> buchGenres = buch.genres().keySet();
        if (buchGenres.isEmpty()) {
            return 0.0;
        }
        for (Long genreId : buchGenres) {
            genreSumme += getGenreGewicht(genreId);
        }
        return genreSumme / buchGenres.size();
    }

    public double getAutorDurchschnitt(BuchInfo buch) {
        double autorSumme = 0.0;
        Set<Long> buchAutoren = buch.autoren().keySet();
        if (buchAutoren.isEmpty()) {
            return 0.0;
        }
        for (Long autorId : buchAutoren) {
            autorSumme += getAutorGewicht(autorId);
        }
        return autorSumme / buchAutoren.size();
    }

    public boolean istUnbekannt(BuchInfo buch) {
        for (Long genreId : buch.genres().keySet()) {
            if (genreGewichte.containsKey(genreId)) {
                return false;
            }
        }
        for (Long autorId : buch.autoren().keySet()) {
            if (autorGewichte.containsKey(autorId)) {
                return false;
            }
        }
        return true;
    }

    private void summiereGewichte(Map<Long, Double> gewichte, Long id, double gewicht) {
        gewichte.merge(id, gewicht, Double::sum);
    }

    private double begrenze(double ursprungsWert, double min, double max) {
        if (ursprungsWert < min) {
            return min;
        } else if (ursprungsWert > max) {
            return max;
        } else {
            return ursprungsWert;
        }
    }
}
