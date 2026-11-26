package com.softwareprojekt.gruppe8.bibliothek.service;

import com.softwareprojekt.gruppe8.bibliothek.entity.Bibliothekseintrag;
import com.softwareprojekt.gruppe8.bibliothek.entity.Regal;
import com.softwareprojekt.gruppe8.bibliothek.repository.RegalRepository;
import com.softwareprojekt.gruppe8.konto.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RegalService {

    private final RegalRepository regalRepository;
    private final BibliothekService bibliothekService;

    public RegalService(RegalRepository regalRepository, BibliothekService bibliothekService) {
        this.regalRepository = regalRepository;
        this.bibliothekService = bibliothekService;
    }

    // ---------- Lesen ----------

    @Transactional(readOnly = true)
    public List<Regal> getRegale(User benutzer) {
        return regalRepository.findByBenutzerOrderByNameAsc(benutzer);
    }

    @Transactional(readOnly = true)
    public Regal getRegalMitBuechern(User benutzer, Long regalId) {
        Regal regal = regalRepository.findWithEintraegeById(regalId)
                .orElseThrow(() -> new IllegalArgumentException("Regal nicht gefunden."));
        pruefeBesitzer(benutzer, regal);
        return regal;
    }

    // ---------- Regal verwalten ----------

    public Regal regalAnlegen(User benutzer, String name, String beschreibung) {
        String bereinigterName = namepruefen(name);
        if (regalRepository.existsByBenutzerAndNameIgnoreCase(benutzer, bereinigterName)) {
            throw new IllegalStateException("Du hast bereits ein Regal mit diesem Namen.");
        }

        Regal regal = new Regal();
        regal.setBenutzer(benutzer);
        regal.setName(bereinigterName);
        regal.setBeschreibung(beschreibung);
        regal.setErstelltAm(LocalDate.now());
        return regalRepository.save(regal);
    }

    public Regal regalBearbeiten(User benutzer, Long regalId, String name, String beschreibung) {
        Regal regal = regalDesBenutzersLaden(benutzer, regalId);
        String bereinigterName = namepruefen(name);

        boolean nameGeaendert = !regal.getName().equalsIgnoreCase(bereinigterName);
        if (nameGeaendert && regalRepository.existsByBenutzerAndNameIgnoreCase(benutzer, bereinigterName)) {
            throw new IllegalStateException("Du hast bereits ein Regal mit diesem Namen.");
        }

        regal.setName(bereinigterName);
        regal.setBeschreibung(beschreibung);
        return regal;
    }

    public void regalLoeschen(User benutzer, Long regalId) {
        Regal regal = regalDesBenutzersLaden(benutzer, regalId);
        // Die Zuordnungen in regal_eintrag loescht Hibernate automatisch mit,
        // die Buecher selbst bleiben in der Bibliothek.
        regalRepository.delete(regal);
    }

    // ---------- Buecher einsortieren ----------

    public void buchHinzufuegen(User benutzer, Long regalId, Long eintragId) {
        Regal regal = regalDesBenutzersLaden(benutzer, regalId);
        Bibliothekseintrag eintrag = bibliothekService.eintragDesBenutzersLaden(benutzer, eintragId);
        regal.getEintraege().add(eintrag);
    }

    public void buchEntfernen(User benutzer, Long regalId, Long eintragId) {
        Regal regal = regalDesBenutzersLaden(benutzer, regalId);
        Bibliothekseintrag eintrag = bibliothekService.eintragDesBenutzersLaden(benutzer, eintragId);
        regal.getEintraege().remove(eintrag);
    }

    /** Fuer den Dialog "Buch verwalten": legt fest, in welchen Regalen ein Buch steht. */
    public void regalZuordnungSetzen(User benutzer, Long eintragId, Set<Long> regalIds) {
        Bibliothekseintrag eintrag = bibliothekService.eintragDesBenutzersLaden(benutzer, eintragId);
        for (Regal regal : regalRepository.findByBenutzerOrderByNameAsc(benutzer)) {
            if (regalIds.contains(regal.getId())) {
                regal.getEintraege().add(eintrag);
            } else {
                regal.getEintraege().remove(eintrag);
            }
        }
    }

    // ---------- Hilfsmethoden ----------

    private Regal regalDesBenutzersLaden(User benutzer, Long regalId) {
        Regal regal = regalRepository.findById(regalId)
                .orElseThrow(() -> new IllegalArgumentException("Regal nicht gefunden."));
        pruefeBesitzer(benutzer, regal);
        return regal;
    }

    private void pruefeBesitzer(User benutzer, Regal regal) {
        if (!regal.getBenutzer().getId().equals(benutzer.getId())) {
            throw new IllegalStateException("Dieses Regal gehoert nicht dir.");
        }
    }

    private String namepruefen(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Das Regal braucht einen Namen.");
        }
        return name.trim();
    }
}