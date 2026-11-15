package com.softwareprojekt.gruppe8.katalog.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String bezeichnung;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private Set<Buch> buecher = new HashSet<>();

    public Genre() {}

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Set<Buch> getBuecher() {
        return buecher;
    }

    public void setBuecher(Set<Buch> buecher) {
        this.buecher = buecher;
    }
}
