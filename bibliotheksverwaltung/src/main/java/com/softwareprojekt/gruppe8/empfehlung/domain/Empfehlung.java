package com.softwareprojekt.gruppe8.empfehlung.domain;

import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.konto.entity.User;
import jakarta.persistence.*;


/**
 * Ein einzelnes Buch, das einem Benutzer im Rahmen der Empfehlungsseite empfohlen wird.
 * Wird dasselbe Buch spaeter erneut empfohlen, entsteht eine neue Empfehlung.
 *
 * @author Niklas Prell
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"benutzer_id", "position"}))
public class Empfehlung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empfehlungId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "benutzer_id", nullable = false)
    private User benutzer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buch_id", nullable = false)
    private Buch buch;

    @Column(nullable = false)
    private String grund;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Feedback feedback;

    @Column(nullable = false)
    private double score;

    @Column(nullable = false)
    private int position;

    @Column(nullable = false)
    private boolean istExploration;

    //Parameterloser Konstruktor fuer JPA
    protected Empfehlung() {
        super();
    }

    public Empfehlung(User benutzer, Buch buch, String grund, double score, int position, boolean istExploration) {
        this.benutzer = benutzer;
        this.buch = buch;
        this.grund = grund;
        this.feedback = Feedback.NICHT_BEWERTET;
        this.score = score;
        this.position = position;
        this.istExploration = istExploration;
    }

    public Long getEmpfehlungId() {
        return empfehlungId;
    }

    public User getBenutzer() {
        return benutzer;
    }

    public Buch getBuch() {
        return buch;
    }

    public String getGrund() {
        return grund;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public double getScore() {
        return score;
    }

    public int getPosition() {
        return position;
    }

    public boolean istExploration() {
        return istExploration;
    }
}
