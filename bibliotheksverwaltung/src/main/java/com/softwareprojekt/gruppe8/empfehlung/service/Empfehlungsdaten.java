package com.softwareprojekt.gruppe8.empfehlung.service;

import java.util.List;

/**
 * Kapselung aller Daten, die als Empfehlungskarte angezeigt werden.
 *
 * @author Niklas Prell
 */

public final class Empfehlungsdaten {
    private final Long empfehlungId;
    private final String buchTitel;
    private final String autor;
    private final List<String> buchGenres;
    private final String buchCoverUrl;
    private final String empfehlungsgrund;
    private final boolean istExploration;

    public Empfehlungsdaten(Long empfehlungId, String buchTitel, String autor, List<String> buchGenres, String buchCoverUrl,
                            String empfehlungsgrund, boolean istExploration) {
        this.empfehlungId = empfehlungId;
        this.buchTitel = buchTitel;
        this.autor = autor;
        this.buchGenres = List.copyOf(buchGenres);
        this.buchCoverUrl = buchCoverUrl;
        this.empfehlungsgrund = empfehlungsgrund;
        this.istExploration = istExploration;
    }

    public Long getEmpfehlungId() {
        return empfehlungId;
    }

    public String getBuchTitel() {
        return buchTitel;
    }

    public String getAutor() {
        return autor;
    }

    public List<String> getBuchGenres() {
        return buchGenres;
    }

    public String getEmpfehlungsgrund() {
        return empfehlungsgrund;
    }

    public String getBuchCoverUrl() {
        return buchCoverUrl;
    }

    public boolean istExploration() {
        return istExploration;
    }
}
