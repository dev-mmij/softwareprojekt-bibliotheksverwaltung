package com.softwareprojekt.gruppe8.tagebuch.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;
import com.softwareprojekt.gruppe8.tagebuch.service.ChallengeService;

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

@Route(value = "challenges", layout = MainLayout.class)
@Menu(title = "Lesechallenges", order = 220)
@PermitAll
public class ChallengeView extends VerticalLayout {

    // Verbindung zur Geschäftslogik
    private final ChallengeService challengeService;

    // Spring übergibt den ChallengeService automatisch
    public ChallengeView(ChallengeService challengeService) {

        this.challengeService = challengeService;

        setWidthFull();
        setPadding(true);
        setSpacing(true);
        getStyle().set("max-width", "1100px");

// Seitenüberschrift im Readly-Design
        H1 titel = new H1("Meine Lesechallenges");

        titel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)")
                .set("color", "var(--readly-text)")
                .set("margin", "0");

        Paragraph beschreibung = new Paragraph(
                "Deine Leseziele und dein Fortschritt im Überblick."
        );

        beschreibung.getStyle()
                .set("color", "var(--readly-text-sekundaer)")
                .set("margin", "0");

        add(titel, beschreibung);

// Jahreschallenge: noch keine ausgewählten Daten
        H3 jahreschallenge = new H3("Jahreschallenge");

        jahreschallenge.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)");

        add(jahreschallenge, erstelleFortschrittskarte());

// Weitere Kennzahlen
        HorizontalLayout kennzahlen = new HorizontalLayout();
        kennzahlen.setWidthFull();

        Div monatsziel = erstelleKennzahl(
                "Monatsziel", "—"
        );

        Div lesetempo = erstelleKennzahl(
                "Aktuelles Lesetempo", "—"
        );

        kennzahlen.add(monatsziel, lesetempo);
        kennzahlen.setFlexGrow(1, monatsziel, lesetempo);

        add(kennzahlen);

// Monatsstatistik
        add(erstelleMonatsstatistik());
    }

    // Erstellt die Fortschrittskarte mit drei Ringen
    private Div erstelleFortschrittskarte() {

        Div karte = erstelleKarte();

        HorizontalLayout inhalt = new HorizontalLayout();
        inhalt.setWidthFull();
        inhalt.setAlignItems(Alignment.CENTER);

// Leeres Kreisdiagramm
// Werte kommen später aus ChallengeStatistik
        Div kreise = new Div();
        kreise.setWidth("260px");
        kreise.setMaxWidth("100%");
        kreise.getStyle().set("flex-shrink", "0");

        kreise.getElement().setProperty(
                "innerHTML",
                """
                <svg xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 240 240"
                role="img"
                aria-label="Noch keine Fortschrittsdaten"
                style="width:100%;height:auto">
                
                <circle cx="120" cy="120" r="100"
                fill="none"
                stroke="#C8963E"
                stroke-opacity="0.2"
                stroke-width="13"/>
                
                <circle cx="120" cy="120" r="76"
                fill="none"
                stroke="#7A2E3A"
                stroke-opacity="0.2"
                stroke-width="13"/>
                
                <circle cx="120" cy="120" r="52"
                fill="none"
                stroke="#719EAA"
                stroke-opacity="0.2"
                stroke-width="13"/>
                
                <text x="120" y="124"
                text-anchor="middle"
                fill="#6E625A"
                font-size="15">
                Keine Daten
                </text>
                </svg>
                """
        );

// Noch keine Statistikwerte
        VerticalLayout werte = new VerticalLayout();
        werte.setPadding(false);
        werte.setSpacing(true);
        werte.setWidthFull();

        werte.add(
                erstelleWert("Gelesen", "#C8963E"),
                erstelleWert("Geplant", "#7A2E3A"),
                erstelleWert("Wunschliste", "#719EAA")
        );

        inhalt.add(kreise, werte);
        inhalt.setFlexGrow(1, werte);

        karte.add(inhalt);

        return karte;
    }

    // Erstellt einen einzelnen Statistikwert
    private Div erstelleWert(
            String bezeichnung,
            String farbe) {

        Div container = new Div();

        Span wert = new Span("—");

        wert.getStyle()
                .set("font-size", "2rem")
                .set("font-weight", "600")
                .set("color", farbe);

        Span label = new Span(bezeichnung);

        label.getStyle()
                .set("display", "block")
                .set("color",
                        "var(--readly-text-sekundaer)");

        container.add(wert, label);

        return container;
    }

    // Erstellt eine Kennzahlenkarte
    private Div erstelleKennzahl(
            String bezeichnung,
            String wert) {

        Div karte = erstelleKarte();
        karte.getStyle().set("flex", "1");

        Span zahl = new Span(wert);

        zahl.getStyle()
                .set("display", "block")
                .set("font-size", "2rem")
                .set("text-align", "center");

        Span label = new Span(bezeichnung);

        label.getStyle()
                .set("display", "block")
                .set("text-align", "center")
                .set("color",
                        "var(--readly-text-sekundaer)");

        karte.add(zahl, label);

        return karte;
    }

    // Erstellt das leere Monatsdiagramm
    private Div erstelleMonatsstatistik() {

        Div karte = erstelleKarte();
        karte.setWidthFull();

        H3 titel = new H3("Gelesene Bücher pro Monat");

        titel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)");

        HorizontalLayout monate = new HorizontalLayout();
        monate.setWidthFull();
        monate.setSpacing(false);

        String[] namen = {
                "Jan", "Feb", "Mär", "Apr",
                "Mai", "Jun", "Jul", "Aug",
                "Sep", "Okt", "Nov", "Dez"
        };

        for (String name : namen) {

            VerticalLayout monat = new VerticalLayout();

            monat.setPadding(false);
            monat.setSpacing(false);
            monat.setAlignItems(Alignment.CENTER);

            Div diagrammbereich = new Div();
            diagrammbereich.setHeight("100px");
            diagrammbereich.setWidth("100%");

            diagrammbereich.getStyle()
                    .set("display", "flex")
                    .set("align-items", "end")
                    .set("justify-content", "center");

// Grundlinie ohne erfundene Werte
            Div grundlinie = new Div();

            grundlinie.getStyle()
                    .set("width", "45%")
                    .set("height", "2px")
                    .set("background",
                            "var(--readly-rahmen)");

            diagrammbereich.add(grundlinie);

            Span monatsname = new Span(name);

            monatsname.getStyle()
                    .set("font-size", "0.75rem")
                    .set("color",
                            "var(--readly-text-sekundaer)");

            monat.add(diagrammbereich, monatsname);

            monate.add(monat);
            monate.setFlexGrow(1, monat);
        }

        Paragraph hinweis = new Paragraph(
                "Noch keine Lesestatistik vorhanden."
        );

        hinweis.getStyle()
                .set("color", "var(--readly-text-sekundaer)");

        karte.add(titel, monate, hinweis);

        return karte;
    }

    // Einheitlicher Kartenstil für Readly
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
