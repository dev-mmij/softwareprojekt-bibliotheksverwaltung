package com.softwareprojekt.gruppe8.bibliothek.service;

import com.softwareprojekt.gruppe8.bibliothek.entity.Bibliothekseintrag;
import com.softwareprojekt.gruppe8.bibliothek.entity.Lesestatus;
import com.softwareprojekt.gruppe8.bibliothek.entity.Regal;
import com.softwareprojekt.gruppe8.bibliothek.repository.BibliothekseintragRepository;
import com.softwareprojekt.gruppe8.bibliothek.repository.RegalRepository;
import com.softwareprojekt.gruppe8.bibliothek.repository.WunschlisteneintragRepository;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.konto.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class BibliothekService {

    private final BibliothekseintragRepository eintragRepository;
    private final RegalRepository regalRepository;
    private final WunschlisteneintragRepository wunschlisteRepository;

    public BibliothekService(BibliothekseintragRepository eintragRepository,
                             RegalRepository regalRepository,
                             WunschlisteneintragRepository wunschlisteRepository) {
        this.eintragRepository = eintragRepository;
        this.regalRepository = regalRepository;
        this.wunschlisteRepository = wunschlisteRepository;
    }

    // ---------- Lesen ----------

    @Transactional(readOnly = true)
    public List<Bibliothekseintrag> getBibliothek(User benutzer) {
        return eintragRepository.findByBenutzer(benutzer);
    }

    @Transactional(readOnly = true)
    public List<Bibliothekseintrag> getReadlist(User benutzer) {
        return eintragRepository.findByBenutzerAndLesestatusOrderByReadlistPositionAsc(
                benutzer, Lesestatus.MOECHTE_ICH_LESEN);
    }

    @Transactional(readOnly = true)
    public boolean istInBibliothek(User benutzer, Buch buch) {
        return eintragRepository.existsByBenutzerAndBuch(benutzer, buch);
    }

    // ---------- Hinzufuegen ----------

    public Bibliothekseintrag buchHinzufuegen(User benutzer, Buch buch, Lesestatus lesestatus) {
        if (eintragRepository.existsByBenutzerAndBuch(benutzer, buch)) {
            throw new IllegalStateException("Dieses Buch ist bereits in deiner Bibliothek.");
        }

        // Ein Buch steht entweder auf der Wunschliste oder in der Bibliothek
        wunschlisteRepository.deleteByBenutzerAndBuch(benutzer, buch);

        Bibliothekseintrag eintrag = new Bibliothekseintrag();
        eintrag.setBenutzer(benutzer);
        eintrag.setBuch(buch);
        eintrag.setHinzugefuegtAm(LocalDate.now());
        statusAnwenden(eintrag, lesestatus);

        return eintragRepository.save(eintrag);
    }

    // ---------- Lesestatus ----------

    public Bibliothekseintrag lesestatusAendern(User benutzer, Long eintragId, Lesestatus neuerStatus) {
        Bibliothekseintrag eintrag = eintragDesBenutzersLaden(benutzer, eintragId);
        Lesestatus alterStatus = eintrag.getLesestatus();

        if (alterStatus == neuerStatus) {
            return eintrag;
        }

        statusAnwenden(eintrag, neuerStatus);
        Bibliothekseintrag gespeichert = eintragRepository.save(eintrag);

        // Wenn ein Buch die Readlist verlaesst, Luecken in der Reihenfolge schliessen
        if (alterStatus == Lesestatus.MOECHTE_ICH_LESEN) {
            readlistNeuNummerieren(benutzer);
        }
        return gespeichert;
    }

    // ---------- Readlist-Reihenfolge ----------

    public void readlistReihenfolgeAendern(User benutzer, List<Long> eintragIdsInNeuerReihenfolge) {
        List<Bibliothekseintrag> readlist = getReadlist(benutzer);
        Map<Long, Bibliothekseintrag> nachId = readlist.stream()
                .collect(Collectors.toMap(Bibliothekseintrag::getId, Function.identity()));

        if (nachId.size() != eintragIdsInNeuerReihenfolge.size()
                || !nachId.keySet().containsAll(eintragIdsInNeuerReihenfolge)) {
            throw new IllegalArgumentException("Die neue Reihenfolge passt nicht zur Readlist.");
        }

        for (int i = 0; i < eintragIdsInNeuerReihenfolge.size(); i++) {
            nachId.get(eintragIdsInNeuerReihenfolge.get(i)).setReadlistPosition(i + 1);
        }
    }

    // ---------- Entfernen ----------

    public void buchEntfernen(User benutzer, Long eintragId) {
        Bibliothekseintrag eintrag = eintragDesBenutzersLaden(benutzer, eintragId);
        boolean warAufReadlist = eintrag.getLesestatus() == Lesestatus.MOECHTE_ICH_LESEN;

        // Zuerst aus allen Regalen nehmen, sonst verweist regal_eintrag noch darauf
        for (Regal regal : regalRepository.findByBenutzerOrderByNameAsc(benutzer)) {
            regal.getEintraege().remove(eintrag);
        }

        eintragRepository.delete(eintrag);

        if (warAufReadlist) {
            readlistNeuNummerieren(benutzer);
        }
    }

    // ---------- Hilfsmethoden ----------

    public Bibliothekseintrag eintragDesBenutzersLaden(User benutzer, Long eintragId) {
        Bibliothekseintrag eintrag = eintragRepository.findById(eintragId)
                .orElseThrow(() -> new IllegalArgumentException("Bibliothekseintrag nicht gefunden."));

        if (!eintrag.getBenutzer().getId().equals(benutzer.getId())) {
            throw new IllegalStateException("Dieser Eintrag gehoert nicht zu deiner Bibliothek.");
        }
        return eintrag;
    }

    /** Setzt den Status und alles, was automatisch dazugehoert (Daten, Readlist-Position). */
    private void statusAnwenden(Bibliothekseintrag eintrag, Lesestatus neuerStatus) {
        LocalDate heute = LocalDate.now();

        // Position berechnen, BEVOR der neue Status gesetzt wird (sonst zaehlt sich der Eintrag selbst mit)
        Integer neuePosition = null;
        if (neuerStatus == Lesestatus.MOECHTE_ICH_LESEN) {
            long anzahl = eintragRepository.countByBenutzerAndLesestatus(
                    eintrag.getBenutzer(), Lesestatus.MOECHTE_ICH_LESEN);
            neuePosition = (int) anzahl + 1;
        }

        eintrag.setLesestatus(neuerStatus);
        eintrag.setReadlistPosition(neuePosition);

        if (neuerStatus == Lesestatus.LESE_ICH_GERADE) {
            if (eintrag.getBegonnenAm() == null) {
                eintrag.setBegonnenAm(heute);
            }
            eintrag.setBeendetAm(null); // falls ein Buch erneut gelesen wird
        }

        if (neuerStatus == Lesestatus.GELESEN || neuerStatus == Lesestatus.ABGEBROCHEN) {
            eintrag.setBeendetAm(heute);
        }
    }

    private void readlistNeuNummerieren(User benutzer) {
        List<Bibliothekseintrag> readlist = getReadlist(benutzer);
        for (int i = 0; i < readlist.size(); i++) {
            readlist.get(i).setReadlistPosition(i + 1);
        }
    }
}