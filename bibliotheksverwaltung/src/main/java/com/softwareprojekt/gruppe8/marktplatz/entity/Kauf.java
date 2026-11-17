package com.softwareprojekt.gruppe8.marktplatz.entity;
import com.softwareprojekt.gruppe8.data.User;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity 
@Table(name = "kauf")

public class Kauf {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "kauf_id")
    private Long id;

    public enum Zahlungsart {
        PayPal, Überweisung, BarzahlungBeiAbholung
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kaeufer_id", nullable = false)
    private User kaeufer;

    @OneToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "angebot_id", nullable = false, unique = true)
    private Angebot angebot;

    @Column (name = "endpreis", nullable = false)
    private BigDecimal endpreis;

    @Enumerated (EnumType.STRING)
    @Column (name = "zahlungsart", nullable = false)
    private Zahlungsart zahlungsart;

    public Kauf() {}

    public Kauf(User kaeufer, Angebot angebot, Zahlungsart zahlungsart) {
        this.kaeufer = kaeufer;
        this.angebot = angebot;
        this.endpreis = angebot.getPreis();
        this.zahlungsart = zahlungsart;
    }

    public Long getId() {
        return id;
    }

    public User getKaeufer() {
        return kaeufer;
    }

    public Angebot getAngebot() {
        return angebot;
    }

    public BigDecimal getEndpreis() {
        return endpreis;
    }

    public Zahlungsart getZahlungsart() {
        return zahlungsart;
    }

    public void setZahlungsart(Zahlungsart zahlungsart) {
        this.zahlungsart = zahlungsart;
    }


}
