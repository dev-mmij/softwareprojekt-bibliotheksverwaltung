package com.softwareprojekt.gruppe8.konto.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "rollen")
public class Rolle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rollen_id")
    private Long rollenId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Rollenname", nullable = false, unique = true, length = 20)
    private RoleType Rollenname;

    @Column(name = "beschreibung", length = 255)
    private String beschreibung;

    protected Rolle() {
        // Wird von JPA benötigt
    }

    public Rolle(RoleType Rollenname, String beschreibung) {
        this.Rollenname = Rollenname;
        this.beschreibung = beschreibung;
    }

    public Long getRollenId() {
        return rollenId;
    }

    public RoleType getRollenname() {
        return Rollenname;
    }

    public void setName(RoleType Rollenname) {
        this.Rollenname = Rollenname;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }
}