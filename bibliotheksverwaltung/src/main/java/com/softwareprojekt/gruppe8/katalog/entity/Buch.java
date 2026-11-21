package com.softwareprojekt.gruppe8.katalog.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "buch")
public class Buch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buch_id")
    private Long id;

    @Column(nullable = false)
    private String titel;

    @Column(unique = true)
    private String isbn;

    private Integer seitenzahl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verlag_id")
    private Verlag verlag;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "buch_autor",
        joinColumns = @JoinColumn(name = "buch_id"),
        inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autoren = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "buch_genre",
        joinColumns = @JoinColumn(name = "buch_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public Buch() {}

    // Getter & Setter (evtl. doch setID())
    public Long getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getSeitenzahl() {
        return seitenzahl;
    }

    public void setSeitenzahl(Integer seitenzahl) {
        this.seitenzahl = seitenzahl;
    }

    public Verlag getVerlag() {
        return verlag;
    }

    public void setVerlag(Verlag verlag) {
        this.verlag = verlag;
    }

    public Set<Autor> getAutoren() {
        return autoren;
    }

    public void setAutoren(Set<Autor> autoren) {
        this.autoren = autoren;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
