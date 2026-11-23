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

    private String username;
    private String EMail;

    @JsonIgnore
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<RoleType> roles;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Set<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }
}