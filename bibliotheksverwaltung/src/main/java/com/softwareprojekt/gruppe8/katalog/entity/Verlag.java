package com.softwareprojekt.gruppe8.katalog.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "verlag")
public class Verlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verlag_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "verlag", fetch = FetchType.LAZY)
    private List<Buch> buecher = new ArrayList<>();

    public Verlag() {}

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

    public List<Buch> getBuecher() {
        return buecher;
    }

    public void setBuecher(List<Buch> buecher) {
        this.buecher = buecher;
    }
}
