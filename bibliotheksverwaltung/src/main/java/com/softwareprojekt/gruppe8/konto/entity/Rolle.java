package com.softwareprojekt.gruppe8.konto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rollen")
public class Rolle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rollen_id")
    private Long rollenId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rollenname", nullable = false, unique = true, length = 20)
    private RoleType name;

    @Column(name = "beschreibung", length = 255)
    private String beschreibung;

    @OneToMany(mappedBy = "rolle", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> user = new HashSet<>();

    protected Rolle() {
        // Wird von JPA benötigt
    }

    public Rolle(RoleType name, String beschreibung) {
        this.name = name;
        this.beschreibung = beschreibung;
    }

    public Long getRollenId() {
        return rollenId;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Set<User> getUser() {
        return user;
    }

    public void setUser(Set<User> user) {
        this.user = user;
    }
}