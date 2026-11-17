package com.softwareprojekt.gruppe8.konto;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Freundschaftsanfrage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User absender;

    @ManyToOne
    private User empfaenger;

    @Enumerated(EnumType.STRING)
    private Anfragestatus status;

    private LocalDateTime anfragedatum;

    public Freundschaftsanfrage() {
    }

    public User getAbsender() {
        return absender;
    }

    public void setAbsender(User absender) {
        this.absender = absender;
    }

    public User getEmpfaenger() {
        return empfaenger;
    }

    public void setEmpfaenger(User empfaenger) {
        this.empfaenger = empfaenger;
    }

    public Anfragestatus getStatus() {
        return status;
    }

    public void setStatus(Anfragestatus status) {
        this.status = status;
    }

    public LocalDateTime getAnfragedatum() {
        return anfragedatum;
    }

    public void setAnfragedatum(LocalDateTime anfragedatum) {
        this.anfragedatum = anfragedatum;
    }
}