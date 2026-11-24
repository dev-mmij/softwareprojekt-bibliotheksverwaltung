package com.softwareprojekt.gruppe8.bibliothek.service;

import com.softwareprojekt.gruppe8.bibliothek.entity.Bibliothekseintrag;
import com.softwareprojekt.gruppe8.bibliothek.entity.Lesestatus;
import com.softwareprojekt.gruppe8.bibliothek.entity.Wunschlisteneintrag;
import com.softwareprojekt.gruppe8.bibliothek.repository.WunschlisteneintragRepository;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.konto.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class WunschlisteService {

    private final WunschlisteneintragRepository wunschlisteRepository;
    private final BibliothekService bibliothekService;

    public WunschlisteService(WunschlisteneintragRepository wunschlisteRepository,
                              BibliothekService bibliothekService) {
        this.wunschlisteRepository = wunschlisteRepository;
        this.bibliothekService = bibliothekService;
    }

    // ---------- Lesen ----------

    @Transactional(readOnly = true)
    public List<Wunschlisteneintrag> getWunschliste(User benutzer) {
        return wunschlisteRepository.findByBenutzerOrderByHinzugefuegtAmDesc(benutzer);
    }

    @Transactional(readOnly = true)
    public boolean istAufWunschliste(User benutzer, Buch buch) {
        return wunschlisteRepository.existsByBenutzerAndBuch(benutzer, buch);
    }

    // ---------- Hinzufuegen / Entfernen ----------

    public Wunschlisteneintrag buchHinzufuegen(User benutzer, Buch buch) {
        if (bibliothekService.istInBibliothek(benutzer, buch)) {
            throw new IllegalStateException("Dieses Buch besitzt du bereits.");
        }
        if (wunschlisteRepository.existsByBenutzerAndBuch(benutzer, buch)) {
            throw new IllegalStateException("Dieses Buch steht bereits auf deiner Wunschliste.");
        }

        Wunschlisteneintrag wunsch = new Wunschlisteneintrag();
        wunsch.setBenutzer(benutzer);
        wunsch.setBuch(buch);
        wunsch.setHinzugefuegtAm(LocalDate.now());
        return wunschlisteRepository.save(wunsch);
    }

    public void buchEntfernen(User benutzer, Long wunschId) {
        Wunschlisteneintrag wunsch = wunschDesBenutzersLaden(benutzer, wunschId);
        wunschlisteRepository.delete(wunsch);
    }

    // ---------- In Bibliothek uebernehmen ----------

    /** Buch wurde gekauft/geschenkt: von der Wunschliste in die Bibliothek. */
    public Bibliothekseintrag inBibliothekUebernehmen(User benutzer, Long wunschId, Lesestatus lesestatus) {
        Wunschlisteneintrag wunsch = wunschDesBenutzersLaden(benutzer, wunschId);
        // buchHinzufuegen entfernt den Wunsch automatisch von der Wunschliste
        return bibliothekService.buchHinzufuegen(benutzer, wunsch.getBuch(), lesestatus);
    }

    // ---------- Hilfsmethoden ----------

    private Wunschlisteneintrag wunschDesBenutzersLaden(User benutzer, Long wunschId) {
        Wunschlisteneintrag wunsch = wunschlisteRepository.findById(wunschId)
                .orElseThrow(() -> new IllegalArgumentException("Wunschlisteneintrag nicht gefunden."));
        if (!wunsch.getBenutzer().getId().equals(benutzer.getId())) {
            throw new IllegalStateException("Dieser Eintrag gehoert nicht zu deiner Wunschliste.");
        }
        return wunsch;
    }
}