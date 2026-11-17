package com.softwareprojekt.gruppe8.bibliothek.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Zusammengesetzter Schluessel der Wunschliste: (benutzer, buch).
 * Die Feldnamen muessen genau zu den @Id-Feldern in Wunschlisteneintrag passen.
 */
public class WunschlisteneintragId implements Serializable {

    private Long benutzer;
    private Long buch;

    public WunschlisteneintragId() {}

    public WunschlisteneintragId(Long benutzer, Long buch) {
        this.benutzer = benutzer;
        this.buch = buch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WunschlisteneintragId that)) return false;
        return Objects.equals(benutzer, that.benutzer) && Objects.equals(buch, that.buch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benutzer, buch);
    }
}