package com.softwareprojekt.gruppe8.konto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softwareprojekt.gruppe8.data.AbstractEntity;
import com.softwareprojekt.gruppe8.katalog.entity.Autor;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application_user")
public class User extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String hashedPassword;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rollen_id", nullable = false)
    private Rolle rolle;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Profil profil;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_autoren",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> gefolgteAutoren = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Rolle getRolle() {
        return rolle;
    }

    public void setRolle(Rolle rolle) {
        this.rolle = rolle;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public Set<Autor> getGefolgteAutoren() {
        return gefolgteAutoren;
    }

    public void setGefolgteAutoren(Set<Autor> gefolgteAutoren) {
        this.gefolgteAutoren = gefolgteAutoren;
    }

    public void autorFolgen(Autor autor) {
        gefolgteAutoren.add(autor);
        autor.getFollower().add(this);
    }

    public void autorNichtMehrFolgen(Autor autor) {
        gefolgteAutoren.remove(autor);
        autor.getFollower().remove(this);
    }
}

