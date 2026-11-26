package com.softwareprojekt.gruppe8.konto.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Freundschaft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User nutzer1;

    @ManyToOne
    private User nutzer2;

    private LocalDateTime seit;

    public Freundschaft() {
    }

    public User getNutzer1() {
        return nutzer1;
    }

    public void setNutzer1(User nutzer1) {
        this.nutzer1 = nutzer1;
    }

    public User getNutzer2() {
        return nutzer2;
    }

    public void setNutzer2(User nutzer2) {
        this.nutzer2 = nutzer2;
    }

    public LocalDateTime getSeit() {
        return seit;
    }

    public void setSeit(LocalDateTime seit) {
        this.seit = seit;
    }
}
