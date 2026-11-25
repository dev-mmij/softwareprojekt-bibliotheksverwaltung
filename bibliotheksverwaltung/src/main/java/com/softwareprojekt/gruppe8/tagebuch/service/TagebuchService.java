package com.softwareprojekt.gruppe8.tagebuch.service;

import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.tagebuch.domain.Tagebucheintrag;
import com.softwareprojekt.gruppe8.tagebuch.repository.TagebucheintragRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// Geschäftslogik für das Lesetagebuch
@Service
public class TagebuchService {

    private final TagebucheintragRepository repository;

// =====================================================
// KONSTRUKTOR
// =====================================================

    public TagebuchService(
            TagebucheintragRepository repository) {

        this.repository = repository;
    }


// =====================================================
// 1. NEUEN TAGEBUCHEINTRAG ERSTELLEN
// =====================================================

    @Transactional
    public Tagebucheintrag erstelleEintrag(
            Long benutzerId,
            Buch buch,
            LocalDate datum,
            String notiz,
            Integer geleseneSeiten) {

        pruefeEingaben(
                benutzerId,
                buch,
                datum,
                geleseneSeiten
        );

        Tagebucheintrag eintrag = new Tagebucheintrag(
                benutzerId,
                buch,
                datum,
                notiz,
                geleseneSeiten
        );

        return repository.save(eintrag);
    }


// =====================================================
// 2. ALLE EINTRÄGE EINES BENUTZERS
// =====================================================

    @Transactional(readOnly = true)
    public List<Tagebucheintrag> findeEintraege(
            Long benutzerId) {

        pruefeBenutzerId(benutzerId);

        return repository
                .findByBenutzerIdOrderByDatumDesc(benutzerId);
    }


// =====================================================
// 3. EINTRÄGE IN EINEM ZEITRAUM
// =====================================================

    @Transactional(readOnly = true)
    public List<Tagebucheintrag> findeEintraegeImZeitraum(
            Long benutzerId,
            LocalDate startdatum,
            LocalDate enddatum) {

        pruefeBenutzerId(benutzerId);

        if (startdatum == null || enddatum == null) {
            throw new IllegalArgumentException(
                    "Startdatum und Enddatum müssen angegeben werden."
            );
        }

        if (enddatum.isBefore(startdatum)) {
            throw new IllegalArgumentException(
                    "Das Enddatum darf nicht vor dem Startdatum liegen."
            );
        }

        return repository.findByBenutzerIdAndDatumBetween(
                benutzerId,
                startdatum,
                enddatum
        );
    }


// =====================================================
// 4. TAGEBUCHEINTRAG AKTUALISIEREN
// =====================================================

    @Transactional
    public Tagebucheintrag aktualisiereEintrag(
            Long eintragId,
            Long benutzerId,
            Buch buch,
            LocalDate datum,
            String notiz,
            Integer geleseneSeiten) {

        pruefeEingaben(
                benutzerId,
                buch,
                datum,
                geleseneSeiten
        );

        if (eintragId == null) {
            throw new IllegalArgumentException(
                    "Eine Eintrag-ID ist erforderlich."
            );
        }

        Tagebucheintrag eintrag = repository
                .findById(eintragId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Tagebucheintrag wurde nicht gefunden."
                        )
                );

// Nur der Eigentümer darf seinen Eintrag ändern.
        if (!eintrag.getBenutzerId().equals(benutzerId)) {
            throw new IllegalArgumentException(
                    "Dieser Tagebucheintrag gehört einem anderen Benutzer."
            );
        }

        eintrag.setBuch(buch);
        eintrag.setDatum(datum);
        eintrag.setNotiz(notiz);
        eintrag.setGeleseneSeiten(geleseneSeiten);

        return repository.save(eintrag);
    }


// =====================================================
// 5. TAGEBUCHEINTRAG LÖSCHEN
// =====================================================

    @Transactional
    public void loescheEintrag(
            Long eintragId,
            Long benutzerId) {

        if (eintragId == null) {
            throw new IllegalArgumentException(
                    "Eine Eintrag-ID ist erforderlich."
            );
        }

        pruefeBenutzerId(benutzerId);

        Tagebucheintrag eintrag = repository
                .findById(eintragId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Tagebucheintrag wurde nicht gefunden."
                        )
                );

// Nur der Eigentümer darf seinen Eintrag löschen.
        if (!eintrag.getBenutzerId().equals(benutzerId)) {
            throw new IllegalArgumentException(
                    "Dieser Tagebucheintrag gehört einem anderen Benutzer."
            );
        }

        repository.delete(eintrag);
    }


// =====================================================
// 6. ALLE EINTRÄGE FÜR EIN BESTIMMTES BUCH
// NEU
// =====================================================

    @Transactional(readOnly = true)
    public List<Tagebucheintrag> findeEintraegeFuerBuch(
            Long benutzerId,
            Long buchId) {

        pruefeBenutzerId(benutzerId);
        pruefeBuchId(buchId);

        return repository
                .findByBenutzerIdOrderByDatumDesc(benutzerId)
                .stream()
                .filter(eintrag ->
                        eintrag.getBuch() != null
                                && buchId.equals(
                                eintrag.getBuch().getId()
                        )
                )
                .toList();
    }


// =====================================================
// 7. GESAMTSEITEN FÜR EIN BUCH
// NEU
// =====================================================

    @Transactional(readOnly = true)
    public int berechneGesamtseitenFuerBuch(
            Long benutzerId,
            Long buchId) {

        List<Tagebucheintrag> eintraege =
                findeEintraegeFuerBuch(
                        benutzerId,
                        buchId
                );

        return eintraege.stream()
                .mapToInt(eintrag ->
                        eintrag.getGeleseneSeiten() == null
                                ? 0
                                : eintrag.getGeleseneSeiten()
                )
                .sum();
    }


// =====================================================
// 8. HEUTE GELESENE SEITEN - ALLE BÜCHER
// NEU
// =====================================================

    @Transactional(readOnly = true)
    public int berechneTagesseiten(
            Long benutzerId) {

        pruefeBenutzerId(benutzerId);

        LocalDate heute = LocalDate.now();

        List<Tagebucheintrag> eintraege =
                repository.findByBenutzerIdAndDatumBetween(
                        benutzerId,
                        heute,
                        heute
                );

        return eintraege.stream()
                .mapToInt(eintrag ->
                        eintrag.getGeleseneSeiten() == null
                                ? 0
                                : eintrag.getGeleseneSeiten()
                )
                .sum();
    }


// =====================================================
// 9. HEUTE GELESENE SEITEN - EIN BUCH
// NEU
// =====================================================

    @Transactional(readOnly = true)
    public int berechneTagesseitenFuerBuch(
            Long benutzerId,
            Long buchId) {

        pruefeBenutzerId(benutzerId);
        pruefeBuchId(buchId);

        LocalDate heute = LocalDate.now();

        List<Tagebucheintrag> eintraege =
                repository.findByBenutzerIdAndDatumBetween(
                        benutzerId,
                        heute,
                        heute
                );

        return eintraege.stream()
                .filter(eintrag ->
                        eintrag.getBuch() != null
                                && buchId.equals(
                                eintrag.getBuch().getId()
                        )
                )
                .mapToInt(eintrag ->
                        eintrag.getGeleseneSeiten() == null
                                ? 0
                                : eintrag.getGeleseneSeiten()
                )
                .sum();
    }


// =====================================================
// 10. EINGABEN VALIDIEREN
// =====================================================

    private void pruefeEingaben(
            Long benutzerId,
            Buch buch,
            LocalDate datum,
            Integer geleseneSeiten) {

        pruefeBenutzerId(benutzerId);

        if (buch == null) {
            throw new IllegalArgumentException(
                    "Ein Buch muss ausgewählt werden."
            );
        }

        if (datum == null) {
            throw new IllegalArgumentException(
                    "Ein Datum muss angegeben werden."
            );
        }

        if (datum.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Das Datum darf nicht in der Zukunft liegen."
            );
        }

        if (geleseneSeiten == null || geleseneSeiten <= 0) {
            throw new IllegalArgumentException(
                    "Die gelesenen Seiten müssen größer als null sein."
            );
        }
    }


// =====================================================
// 11. BENUTZER-ID PRÜFEN
// =====================================================

    private void pruefeBenutzerId(Long benutzerId) {

        if (benutzerId == null || benutzerId <= 0) {
            throw new IllegalArgumentException(
                    "Eine gültige Benutzer-ID ist erforderlich."
            );
        }
    }


// =====================================================
// 12. BUCH-ID PRÜFEN
// =====================================================

    private void pruefeBuchId(Long buchId) {

        if (buchId == null || buchId <= 0) {
            throw new IllegalArgumentException(
                    "Eine gültige Buch-ID ist erforderlich."
            );
        }
    }
}