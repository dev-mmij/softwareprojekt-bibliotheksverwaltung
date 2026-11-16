package com.softwareprojekt.gruppe8.empfehlung.domain;

import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import jakarta.persistence.*;


/**
 * Ein einzelnes Buch, das einem Benutzer im Rahmen der Empfehlungsseite empfohlen wird.
 * Wird dasselbe Buch spaeter erneut empfohlen, entsteht eine neue Empfehlung.
 *
 * @author Niklas Prell
 */
@Entity
public class Empfehlung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empfehlungId;

    //TODO: Benutzer hinzufuegen (auch im Konstruktur, Feld und @UniqueConstraint auf benutzer_id + position

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

    public Empfehlung(Buch buch, String grund, double score, int position, boolean istExploration) {
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
