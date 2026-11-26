package com.softwareprojekt.gruppe8.katalog.entity;

import jakarta.persistence.*;
import com.softwareprojekt.gruppe8.konto.entity.User;
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

    @ManyToMany(mappedBy = "gefolgteAutoren", fetch = FetchType.LAZY)
    private Set<User> follower = new HashSet<>();

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

    public Set<User> getFollower() {
        return follower;
    }

    public void setFollower(Set<User> follower) {
        this.follower = follower;
    }

    public Set<Buch> getBuecher() {
        return buecher;
    }

    public void setBuecher(Set<Buch> buecher) {
        this.buecher = buecher;
    }
}
