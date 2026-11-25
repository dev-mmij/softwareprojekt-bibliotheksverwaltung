package com.softwareprojekt.gruppe8.tagebuch.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

// Nur für angemeldete Benutzer und Administratoren
@Route(value = "meine-erfolge", layout = MainLayout.class)
@Menu(title = "Meine Erfolge", order = 230)
@PermitAll
public class MeineErfolgeView extends VerticalLayout {

    public MeineErfolgeView() {

        setWidthFull();
        setMaxWidth("1100px");
        setPadding(true);
        setSpacing(true);

// ================================================
// SEITENÜBERSCHRIFT
// ================================================

        H1 titel = new H1("Meine Erfolge");

        titel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)")
                .set("color", "var(--readly-text)")
                .set("margin", "0");

        Paragraph beschreibung = new Paragraph(
                "Entdecke deine gesammelten Punkte, " +
                        "Abzeichen und erreichten Leseziele."
        );

        beschreibung.getStyle()
                .set("color", "var(--readly-text-sekundaer)")
                .set("margin", "0");

        add(titel, beschreibung);

// ================================================
// PUNKTE UND ABZEICHEN
// ================================================

        HorizontalLayout kennzahlen =
                new HorizontalLayout();

        kennzahlen.setWidthFull();
        kennzahlen.setSpacing(true);

        Div punkte = erstelleKennzahl(
                "Gesamtpunkte",
                "—",
                "★"
        );

        Div abzeichen = erstelleKennzahl(
                "Gesammelte Abzeichen",
                "—",
                "◆"
        );

        kennzahlen.add(punkte, abzeichen);
        kennzahlen.setFlexGrow(1, punkte, abzeichen);

        add(kennzahlen);

// ================================================
// ABZEICHEN-BEREICH
// ================================================

        H3 abzeichenTitel = new H3("Meine Abzeichen");

        abzeichenTitel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)");

        add(abzeichenTitel);

        Div abzeichenKarte = erstelleKarte();

        Paragraph keineAbzeichen = new Paragraph(
                "Noch keine Abzeichen vorhanden."
        );

        keineAbzeichen.getStyle()
                .set("color", "var(--readly-text-sekundaer)")
                .set("text-align", "center");

        abzeichenKarte.add(keineAbzeichen);

        add(abzeichenKarte);

// ================================================
// BELOHNUNGSVERLAUF
// ================================================

        H3 verlaufTitel = new H3("Erhaltene Belohnungen");

        verlaufTitel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)");

        add(verlaufTitel);

        Div verlaufKarte = erstelleKarte();

        Paragraph keinVerlauf = new Paragraph(
                "Noch keine Belohnungen vorhanden."
        );

        keinVerlauf.getStyle()
                .set("color", "var(--readly-text-sekundaer)")
                .set("text-align", "center");

        verlaufKarte.add(keinVerlauf);

        add(verlaufKarte);
    }

// ================================================
// KENNZAHLENKARTE
// ================================================

    private Div erstelleKennzahl(
            String bezeichnung,
            String wert,
            String symbol) {

        Div karte = erstelleKarte();

        karte.getStyle().set("flex", "1");

        Span icon = new Span(symbol);

        icon.getStyle()
                .set("display", "block")
                .set("font-size", "1.8rem")
                .set("color", "var(--readly-akzent)")
                .set("text-align", "center");

        Span zahl = new Span(wert);

        zahl.getStyle()
                .set("display", "block")
                .set("font-size", "2.5rem")
                .set("font-weight", "600")
                .set("color", "var(--readly-text)")
                .set("text-align", "center");

        Span label = new Span(bezeichnung);

        label.getStyle()
                .set("display", "block")
                .set("text-align", "center")
                .set("color",
                        "var(--readly-text-sekundaer)");

        karte.add(icon, zahl, label);

        return karte;
    }

// ================================================
// READLY-KARTENDESIGN
// ================================================

    private Div erstelleKarte() {

        Div karte = new Div();
        karte.setWidthFull();

        karte.getStyle()
                .set("background", "var(--readly-flaeche)")
                .set("border",
                        "1px solid var(--readly-rahmen)")
                .set("border-radius", "16px")
                .set("padding", "24px")
                .set("box-sizing", "border-box");

        return karte;
    }
}

