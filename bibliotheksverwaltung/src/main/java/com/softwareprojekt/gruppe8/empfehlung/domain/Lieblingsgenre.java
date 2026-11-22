package com.softwareprojekt.gruppe8.empfehlung.domain;

import com.softwareprojekt.gruppe8.katalog.entity.Genre;
import com.softwareprojekt.gruppe8.konto.entity.User;
import jakarta.persistence.*;

/**
 * Entitaet zur Speicherung der Lieblingsgenres eines Benutzers
 *
 * @author Niklas Prell
 */


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"benutzer_id", "genre_id"}))
public class Lieblingsgenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lieblingsgenreId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "benutzer_id", nullable = false)
    private User benutzer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    //Parameterloser Konstruktor fuer JPA
    protected Lieblingsgenre() {
        super();
    }

    public Lieblingsgenre(User benutzer, Genre genre) {
        this.benutzer = benutzer;
        this.genre = genre;
    }
    public Long getLieblingsgenreId() {
        return lieblingsgenreId;
    }
    public User getBenutzer() {
        return benutzer;
    }
    public Genre getGenre() {
        return genre;
    }
}
