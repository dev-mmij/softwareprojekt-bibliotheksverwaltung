package com.softwareprojekt.gruppe8.tagebuch.service;

import com.softwareprojekt.gruppe8.tagebuch.domain.ChallengeBelohnung;
import com.softwareprojekt.gruppe8.tagebuch.repository.ChallengeBelohnungRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Geschäftslogik für Challenge-Belohnungen
@Service
public class BelohnungService {

    private final ChallengeBelohnungRepository belohnungRepository;

    // Constructor Injection
    public BelohnungService(
            ChallengeBelohnungRepository belohnungRepository) {

        this.belohnungRepository = belohnungRepository;
    }

// =====================================================
// 1. ALLE BELOHNUNGEN EINES BENUTZERS
// =====================================================

    @Transactional(readOnly = true)
    public List<ChallengeBelohnung> findeBelohnungen(
            Long benutzerId) {

        pruefeBenutzerId(benutzerId);

        return belohnungRepository
                .findByBenutzerId(benutzerId);
    }

// =====================================================
// 2. PRÜFEN, OB EINE BELOHNUNG EXISTIERT
// =====================================================

    @Transactional(readOnly = true)
    public boolean hatBelohnungErhalten(
            Long benutzerId,
            Long challengeId) {

        pruefeBenutzerId(benutzerId);

        if (challengeId == null) {
            throw new IllegalArgumentException(
                    "Die Challenge-ID darf nicht leer sein."
            );
        }

        return belohnungRepository
                .existsByBenutzerIdAndLesechallenge_Id(
                        benutzerId,
                        challengeId
                );
    }

// =====================================================
// 3. GESAMTPUNKTE BERECHNEN
// =====================================================

    @Transactional(readOnly = true)
    public int berechneGesamtpunkte(Long benutzerId) {

        pruefeBenutzerId(benutzerId);

        List<ChallengeBelohnung> belohnungen =
                belohnungRepository
                        .findByBenutzerId(benutzerId);

        return belohnungen.stream()
                .mapToInt(belohnung ->
                        belohnung.getPunkte() == null
                                ? 0
                                : belohnung.getPunkte())
                .sum();
    }

// =====================================================
// 4. ANZAHL DER ABZEICHEN
// =====================================================

    @Transactional(readOnly = true)
    public long zaehleAbzeichen(Long benutzerId) {

        pruefeBenutzerId(benutzerId);

        return belohnungRepository
                .findByBenutzerId(benutzerId)
                .stream()
                .filter(belohnung ->
                        belohnung.getAbzeichenName() != null
                                && !belohnung.getAbzeichenName()
                                .isBlank())
                .count();
    }

// =====================================================
// 5. BELOHNUNGEN EINER CHALLENGE
// =====================================================

    @Transactional(readOnly = true)
    public List<ChallengeBelohnung> findeChallengeBelohnungen(
            Long challengeId) {

        if (challengeId == null) {
            throw new IllegalArgumentException(
                    "Die Challenge-ID darf nicht leer sein."
            );
        }

        return belohnungRepository
                .findByLesechallenge_Id(challengeId);
    }

// =====================================================
// 6. EINGABEN PRÜFEN
// =====================================================

    private void pruefeBenutzerId(Long benutzerId) {

        if (benutzerId == null || benutzerId <= 0) {
            throw new IllegalArgumentException(
                    "Eine gültige Benutzer-ID ist erforderlich."
            );
        }
    }
}
