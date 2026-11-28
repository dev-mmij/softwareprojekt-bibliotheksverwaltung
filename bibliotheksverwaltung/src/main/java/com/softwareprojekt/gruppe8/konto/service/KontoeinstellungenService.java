package com.softwareprojekt.gruppe8.konto.service;

import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.konto.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class KontoeinstellungenService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public KontoeinstellungenService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void emailAendern(String aktuellerUsername, String neueEmail) {
        String bereinigteEmail = neueEmail == null
                ? ""
                : neueEmail.trim().toLowerCase(Locale.ROOT);

        if (bereinigteEmail.isBlank()) {
            throw new IllegalArgumentException(
                    "Bitte gib eine gültige E-Mail-Adresse ein."
            );
        }

        User user = userRepository.findByUsername(aktuellerUsername)
                .orElseThrow(() -> new IllegalStateException(
                        "Das Benutzerkonto wurde nicht gefunden."
                ));

        boolean emailGeaendert =
                !bereinigteEmail.equalsIgnoreCase(user.getEmail());

        if (!emailGeaendert) {
            throw new IllegalArgumentException(
                    "Diese E-Mail-Adresse ist bereits mit deinem Konto verknüpft."
            );
        }

        boolean usernameBelegt =
                !bereinigteEmail.equalsIgnoreCase(user.getUsername())
                        && userRepository.existsByUsername(bereinigteEmail);

        if (userRepository.existsByEmail(bereinigteEmail) || usernameBelegt) {
            throw new IllegalArgumentException(
                    "Diese E-Mail-Adresse wird bereits verwendet."
            );
        }

        user.setEmail(bereinigteEmail);
        user.setUsername(bereinigteEmail);
        userRepository.save(user);
    }

    @Transactional
    public void passwortAendern(
            String username,
            String altesPasswort,
            String neuesPasswort
    ) {
        if (altesPasswort == null || altesPasswort.isBlank()) {
            throw new IllegalArgumentException(
                    "Bitte gib dein aktuelles Passwort ein."
            );
        }

        if (neuesPasswort == null || neuesPasswort.length() < 8) {
            throw new IllegalArgumentException(
                    "Das neue Passwort muss mindestens 8 Zeichen lang sein."
            );
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException(
                        "Das Benutzerkonto wurde nicht gefunden."
                ));

        if (!passwordEncoder.matches(
                altesPasswort,
                user.getHashedPassword()
        )) {
            throw new IllegalArgumentException(
                    "Das aktuelle Passwort ist nicht korrekt."
            );
        }

        user.setHashedPassword(passwordEncoder.encode(neuesPasswort));
        userRepository.save(user);
    }
}