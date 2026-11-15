package com.softwareprojekt.gruppe8.katalog.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "autor_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "autoren", fetch = FetchType.LAZY)
    private Set<Buch> buecher = new HashSet<>();

    public Autor() {}

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Buch> getBuecher() {
        return buecher;
    }

    public void setBuecher(Set<Buch> buecher) {
        this.buecher = buecher;
    }
}
