package com.softwareprojekt.gruppe8.konto.service;

import com.softwareprojekt.gruppe8.konto.entity.RoleType;
import com.softwareprojekt.gruppe8.konto.entity.Rolle;
import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.konto.repository.RolleRepository;
import com.softwareprojekt.gruppe8.konto.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrierungService {

    private final UserRepository userRepository;
    private final RolleRepository rolleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrierungService(
            UserRepository userRepository,
            RolleRepository rolleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.rolleRepository = rolleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registrieren(String email, String passwort) {
        String bereinigteEmail = email == null ? "" : email.trim().toLowerCase();

        if (bereinigteEmail.isBlank()) {
            throw new IllegalArgumentException("Bitte gib eine E-Mail-Adresse ein.");
        }

        if (userRepository.existsByUsername(bereinigteEmail)
                || userRepository.existsByEmail(bereinigteEmail)) {
            throw new IllegalArgumentException(
                    "Für diese E-Mail-Adresse existiert bereits ein Konto."
            );
        }

        Rolle userRolle = rolleRepository.findByName(RoleType.USER)
                .orElseThrow(() -> new IllegalStateException(
                        "Die Standardrolle USER ist nicht in der Datenbank vorhanden."
                ));

        User user = new User();
        user.setUsername(bereinigteEmail);
        user.setEmail(bereinigteEmail);
        user.setHashedPassword(passwordEncoder.encode(passwort));
        user.setRolle(userRolle);

        userRepository.save(user);
    }
}