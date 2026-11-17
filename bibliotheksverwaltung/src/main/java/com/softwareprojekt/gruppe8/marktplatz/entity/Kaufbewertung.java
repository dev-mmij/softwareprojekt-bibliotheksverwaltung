package com.softwareprojekt.gruppe8.marktplatz.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "kaufbewertung")
public class Kaufbewertung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kaufbewertung_id")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "kauf_id", nullable = false, unique = true)
    private Kauf kauf;

    @Column(name = "bewertung", nullable = false)
    private int bewertung;

    @Column(name = "kommentar")
    private String kommentar;

    public Kaufbewertung() {
    }

    public Kaufbewertung(Kauf kauf, int bewertung, String kommentar) {
        this.kauf = kauf;
        setBewertung(bewertung);
        this.kommentar = kommentar;
    }

    public Long getId() {
        return id;
    }

    public Kauf getKauf() {
        return kauf;
    }

    public int getBewertung() {
        return bewertung;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setBewertung(int bewertung) {
        if (bewertung < 1 || bewertung > 5) {
            System.out.println("Ungültige Bewertung. Bitte geben Sie eine Zahl zwischen 1 und 5 ein.");
            return;
        }
        this.bewertung = bewertung;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }
}
