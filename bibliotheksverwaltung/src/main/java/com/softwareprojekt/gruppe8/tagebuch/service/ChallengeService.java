package com.softwareprojekt.gruppe8.tagebuch.service;

import com.softwareprojekt.gruppe8.bibliothek.entity.Bibliothekseintrag;
import com.softwareprojekt.gruppe8.bibliothek.entity.Lesestatus;
import com.softwareprojekt.gruppe8.bibliothek.repository.BibliothekseintragRepository;
import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.tagebuch.datatransfer.ChallengeStatistik;
import com.softwareprojekt.gruppe8.tagebuch.datatransfer.TaeglicheChallengeStatistik;
import com.softwareprojekt.gruppe8.tagebuch.domain.ChallengeTeilnahme;
import com.softwareprojekt.gruppe8.tagebuch.domain.ChallengeTyp;
import com.softwareprojekt.gruppe8.tagebuch.domain.Lesechallenge;
import com.softwareprojekt.gruppe8.tagebuch.domain.Tagebucheintrag;
import com.softwareprojekt.gruppe8.tagebuch.domain.TeilnahmeStatus;
import com.softwareprojekt.gruppe8.tagebuch.repository.ChallengeTeilnahmeRepository;
import com.softwareprojekt.gruppe8.tagebuch.repository.LesechallengeRepository;
import com.softwareprojekt.gruppe8.tagebuch.repository.TagebucheintragRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChallengeService {

// =====================================================
// REPOSITORIES
// =====================================================

    private final LesechallengeRepository challengeRepository;

    private final ChallengeTeilnahmeRepository teilnahmeRepository;

    private final BibliothekseintragRepository bibliothekRepository;

    private final TagebucheintragRepository tagebuchRepository;


// =====================================================
// KONSTRUKTOR
// =====================================================

    public ChallengeService(
            LesechallengeRepository challengeRepository,
            ChallengeTeilnahmeRepository teilnahmeRepository,
            BibliothekseintragRepository bibliothekRepository,
            TagebucheintragRepository tagebuchRepository) {

        this.challengeRepository = challengeRepository;
        this.teilnahmeRepository = teilnahmeRepository;
        this.bibliothekRepository = bibliothekRepository;
        this.tagebuchRepository = tagebuchRepository;
    }


// =====================================================
// 1. ADMIN-BERECHTIGUNG PRÜFEN
// =====================================================

    private void pruefeAdminBerechtigung() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getAuthorities().stream()
                .noneMatch(authority ->
                        "ROLE_ADMIN".equals(
                                authority.getAuthority()
                        ))) {

            throw new AccessDeniedException(
                    "Nur Administratoren dürfen Challenges verwalten."
            );
        }
    }


// =====================================================
// 2. CHALLENGE ERSTELLEN
// =====================================================

    @Transactional
    public Lesechallenge erstelleChallenge(
            String titel,
            String beschreibung,
            ChallengeTyp typ,
            LocalDate startdatum,
            LocalDate enddatum,
            Integer zielWert,
            Integer belohnungPunkte,
            String abzeichenName) {

        pruefeAdminBerechtigung();

        if (titel == null || titel.isBlank()) {
            throw new IllegalArgumentException(
                    "Ein Challenge-Titel ist erforderlich."
            );
        }

        if (typ == null) {
            throw new IllegalArgumentException(
                    "Bitte einen Challenge-Typ auswählen."
            );
        }

        if (startdatum == null || enddatum == null) {
            throw new IllegalArgumentException(
                    "Startdatum und Enddatum sind erforderlich."
            );
        }

        if (enddatum.isBefore(startdatum)) {
            throw new IllegalArgumentException(
                    "Das Enddatum darf nicht vor dem Startdatum liegen."
            );
        }

        if (zielWert == null || zielWert <= 0) {
            throw new IllegalArgumentException(
                    "Der Zielwert muss größer als null sein."
            );
        }

        if (belohnungPunkte == null
                || belohnungPunkte < 0) {

            throw new IllegalArgumentException(
                    "Belohnungspunkte dürfen nicht negativ sein."
            );
        }

        if ((typ == ChallengeTyp.JAHRESCHALLENGE
                || typ == ChallengeTyp.MONATSCHALLENGE)
                && startdatum.getYear() != enddatum.getYear()) {

            throw new IllegalArgumentException(
                    "Diese Challenge muss innerhalb eines Kalenderjahres liegen."
            );
        }

        Lesechallenge challenge = new Lesechallenge(
                titel.trim(),
                beschreibung,
                typ,
                startdatum,
                enddatum,
                zielWert,
                belohnungPunkte,
                abzeichenName
        );

        challenge.setAktiv(true);

        return challengeRepository.save(challenge);
    }


// =====================================================
// 3. ALLE CHALLENGES ANZEIGEN
// =====================================================

    @Transactional(readOnly = true)
    public List<Lesechallenge> findeAlleChallenges() {

        return challengeRepository.findAll();
    }


// =====================================================
// 4. EINZELNE CHALLENGE FINDEN
// =====================================================

    @Transactional(readOnly = true)
    public Lesechallenge findeChallenge(Long challengeId) {

        if (challengeId == null) {
            throw new IllegalArgumentException(
                    "Eine Challenge-ID ist erforderlich."
            );
        }

        return challengeRepository
                .findById(challengeId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Challenge wurde nicht gefunden."
                        )
                );
    }


// =====================================================
// 5. AN EINER CHALLENGE TEILNEHMEN
// =====================================================

    @Transactional
    public ChallengeTeilnahme treteChallengeBei(
            Long benutzerId,
            Long challengeId) {

        pruefeBenutzerId(benutzerId);

        Lesechallenge challenge = findeChallenge(challengeId);

        if (!challenge.isAktiv()) {
            throw new IllegalArgumentException(
                    "Diese Challenge ist nicht mehr aktiv."
            );
        }

        LocalDate heute = LocalDate.now();

        if (challenge.getEnddatum() != null
                && heute.isAfter(challenge.getEnddatum())) {

            throw new IllegalArgumentException(
                    "Diese Challenge ist bereits beendet."
            );
        }

        boolean bereitsAngemeldet =
                teilnahmeRepository
                        .existsByBenutzerIdAndLesechallenge_Id(
                                benutzerId,
                                challengeId
                        );

        if (bereitsAngemeldet) {
            throw new IllegalArgumentException(
                    "Du nimmst bereits an dieser Challenge teil."
            );
        }

        ChallengeTeilnahme teilnahme =
                new ChallengeTeilnahme(
                        benutzerId,
                        challenge,
                        LocalDate.now(),
                        TeilnahmeStatus.AKTIV
                );

        return teilnahmeRepository.save(teilnahme);
    }


// =====================================================
// 6. MEINE TEILNAHMEN
// =====================================================

    @Transactional(readOnly = true)
    public List<ChallengeTeilnahme> findeMeineTeilnahmen(
            Long benutzerId) {

        pruefeBenutzerId(benutzerId);

        return teilnahmeRepository.findByBenutzerId(benutzerId);
    }


// =====================================================
// 7. TEILNAHMESTATUS ÄNDERN
// =====================================================

    @Transactional
    public ChallengeTeilnahme aendereTeilnahmeStatus(
            Long benutzerId,
            Long challengeId,
            TeilnahmeStatus neuerStatus) {

        pruefeBenutzerId(benutzerId);

        if (challengeId == null || neuerStatus == null) {
            throw new IllegalArgumentException(
                    "Challenge und Status sind erforderlich."
            );
        }

        ChallengeTeilnahme teilnahme =
                teilnahmeRepository
                        .findByBenutzerIdAndLesechallenge_Id(
                                benutzerId,
                                challengeId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Teilnahme wurde nicht gefunden."
                                )
                        );

        teilnahme.setStatus(neuerStatus);

        return teilnahmeRepository.save(teilnahme);
    }


// =====================================================
// 8. STATISTIK FÜR JAHRES- UND MONATSCHALLENGES
// =====================================================

    @Transactional(readOnly = true)
    public ChallengeStatistik berechneStatistik(
            User benutzer,
            Lesechallenge challenge) {

        if (benutzer == null || challenge == null) {
            throw new IllegalArgumentException(
                    "Benutzer und Challenge sind erforderlich."
            );
        }

        if (challenge.getTyp()
                == ChallengeTyp.TAEGLICHE_CHALLENGE) {

            return ChallengeStatistik.ohneDaten();
        }

        List<Bibliothekseintrag> eintraege =
                bibliothekRepository.findByBenutzer(benutzer);

        LocalDate start = challenge.getStartdatum();
        LocalDate ende = challenge.getEnddatum();

        int geleseneBuecher = 0;
        int geplanteBuecher = 0;

        List<Integer> buecherProMonat = new ArrayList<>();

        for (int monat = 1; monat <= 12; monat++) {
            buecherProMonat.add(0);
        }

        for (Bibliothekseintrag eintrag : eintraege) {

            if (eintrag.getLesestatus()
                    == Lesestatus.MOECHTE_ICH_LESEN) {

                geplanteBuecher++;
            }

            if (eintrag.getLesestatus()
                    != Lesestatus.GELESEN) {

                continue;
            }

            LocalDate beendetAm = eintrag.getBeendetAm();

            if (beendetAm == null) {
                continue;
            }

            boolean imZeitraum =
                    !beendetAm.isBefore(start)
                            && !beendetAm.isAfter(ende);

            if (!imZeitraum) {
                continue;
            }

            geleseneBuecher++;

            int monatsIndex = beendetAm.getMonthValue() - 1;

            buecherProMonat.set(
                    monatsIndex,
                    buecherProMonat.get(monatsIndex) + 1
            );
        }

        Integer monatsziel = null;

        if (challenge.getTyp()
                == ChallengeTyp.MONATSCHALLENGE) {

            monatsziel = challenge.getZielWert();
        }

// Aktuell keine verlässliche Berechnung
// des Lesetempos vorhanden.
        Double lesetempo = null;

        return new ChallengeStatistik(
                challenge.getZielWert(),
                geleseneBuecher,
                geplanteBuecher,
                geplanteBuecher,
                monatsziel,
                lesetempo,
                buecherProMonat
        );
    }


// =====================================================
// 9. STATISTIK FÜR TÄGLICHE CHALLENGES
// =====================================================

    @Transactional(readOnly = true)
    public TaeglicheChallengeStatistik berechneTaeglicheStatistik(
            Long benutzerId,
            Lesechallenge challenge) {

        pruefeBenutzerId(benutzerId);

        if (challenge == null
                || challenge.getTyp()
                != ChallengeTyp.TAEGLICHE_CHALLENGE) {

            throw new IllegalArgumentException(
                    "Eine tägliche Challenge ist erforderlich."
            );
        }

        int zielSeitenProTag = challenge.getZielWert();

        LocalDate start = challenge.getStartdatum();
        LocalDate ende = challenge.getEnddatum();
        LocalDate heute = LocalDate.now();

        LocalDate letzterTag =
                heute.isBefore(ende) ? heute : ende;

        if (letzterTag.isBefore(start)) {

            return new TaeglicheChallengeStatistik(
                    zielSeitenProTag,
                    0,
                    0,
                    Map.of()
            );
        }

        List<Tagebucheintrag> eintraege =
                tagebuchRepository
                        .findByBenutzerIdAndDatumBetween(
                                benutzerId,
                                start,
                                letzterTag
                        );

        Map<LocalDate, Integer> seitenProTag =
                new HashMap<>();

        for (Tagebucheintrag eintrag : eintraege) {

            if (eintrag.getDatum() == null) {
                continue;
            }

            Integer seiten = eintrag.getGeleseneSeiten();

            if (seiten == null || seiten <= 0) {
                continue;
            }

            seitenProTag.merge(
                    eintrag.getDatum(),
                    seiten,
                    Integer::sum
            );
        }

        long vergangeneTage =
                ChronoUnit.DAYS.between(
                        start,
                        letzterTag
                ) + 1;

        long erfolgreicheTage = 0;

        for (LocalDate datum = start;
             !datum.isAfter(letzterTag);
             datum = datum.plusDays(1)) {

            int geleseneSeiten =
                    seitenProTag.getOrDefault(datum, 0);

            if (geleseneSeiten >= zielSeitenProTag) {
                erfolgreicheTage++;
            }
        }

        return new TaeglicheChallengeStatistik(
                zielSeitenProTag,
                vergangeneTage,
                erfolgreicheTage,
                seitenProTag
        );
    }


// =====================================================
// 10. CHALLENGE DEAKTIVIEREN (NEU)
// =====================================================

    @Transactional
    public void deaktiviereChallenge(Long challengeId) {

// Nur ADMIN darf Challenges deaktivieren.
        pruefeAdminBerechtigung();

        if (challengeId == null) {
            throw new IllegalArgumentException(
                    "Eine Challenge-ID ist erforderlich."
            );
        }

        Lesechallenge challenge = challengeRepository
                .findById(challengeId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Challenge wurde nicht gefunden."
                        )
                );

        if (!challenge.isAktiv()) {
            throw new IllegalArgumentException(
                    "Diese Challenge ist bereits deaktiviert."
            );
        }

// Challenge nur deaktivieren, nicht löschen.
        challenge.setAktiv(false);

        challengeRepository.save(challenge);
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
}