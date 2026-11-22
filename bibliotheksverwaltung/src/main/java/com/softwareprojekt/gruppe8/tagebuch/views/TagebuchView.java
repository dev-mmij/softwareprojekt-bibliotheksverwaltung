package com.softwareprojekt.gruppe8.tagebuch.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;

import com.softwareprojekt.gruppe8.security.AuthenticatedUser;

import com.softwareprojekt.gruppe8.konto.User;

import com.softwareprojekt.gruppe8.katalog.entity.Buch;

import com.softwareprojekt.gruppe8.bibliothek.entity.Bibliothekseintrag;
import com.softwareprojekt.gruppe8.bibliothek.entity.Lesestatus;
import com.softwareprojekt.gruppe8.bibliothek.repository.BibliothekseintragRepository;

import com.softwareprojekt.gruppe8.tagebuch.service.TagebuchService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

import java.util.List;

@Route(value = "tagebuch", layout = MainLayout.class)
@Menu(title = "Mein Lesetagebuch", order = 210)
@PermitAll
public class TagebuchView extends VerticalLayout {

    private final BibliothekseintragRepository bibliothekRepository;
    private final TagebuchService tagebuchService;
    private final AuthenticatedUser authenticatedUser;

    private final VerticalLayout buchListe =
            new VerticalLayout();

    private final Span heuteGesamt =
            new Span("—");

    private final Span aktiveBuecher =
            new Span("—");


// =====================================================
// KONSTRUKTOR
// =====================================================

    public TagebuchView(
            BibliothekseintragRepository bibliothekRepository,
            TagebuchService tagebuchService,
            AuthenticatedUser authenticatedUser) {

        this.bibliothekRepository = bibliothekRepository;
        this.tagebuchService = tagebuchService;
        this.authenticatedUser = authenticatedUser;

        setWidthFull();
        setMaxWidth("1100px");
        setPadding(true);
        setSpacing(true);

        getStyle().set("margin", "0 auto");

        H1 titel = new H1("Mein Lesetagebuch");

        titel.getStyle()
                .set(
                        "font-family",
                        "var(--readly-schrift-ueberschrift)"
                )
                .set("color", "var(--readly-text)")
                .set("margin-bottom", "0");

        Paragraph beschreibung = new Paragraph(
                "Verfolge deinen Lesefortschritt für jedes Buch."
        );

        beschreibung.getStyle()
                .set("color", "var(--readly-text-sekundaer)")
                .set("margin-top", "0");

        H2 bereichTitel = new H2("Aktuell lese ich");

        bereichTitel.getStyle()
                .set(
                        "font-family",
                        "var(--readly-schrift-ueberschrift)"
                );

        buchListe.setWidthFull();
        buchListe.setPadding(false);
        buchListe.setSpacing(true);

        HorizontalLayout statistik =
                new HorizontalLayout();

        statistik.setWidthFull();

        Div tagesKarte = erstelleStatistikKarte(
                "Heute insgesamt",
                heuteGesamt,
                "Seiten"
        );

        Div buecherKarte = erstelleStatistikKarte(
                "Aktive Bücher",
                aktiveBuecher,
                "Bücher"
        );

        statistik.add(tagesKarte, buecherKarte);
        statistik.setFlexGrow(1, tagesKarte, buecherKarte);

        add(
                titel,
                beschreibung,
                bereichTitel,
                buchListe,
                statistik
        );

        ladeTagebuch();
    }


// =====================================================
// 1. ANGEMELDETEN BENUTZER ERMITTELN
// =====================================================

    private User ermittleBenutzer() {

        Object angemeldeterBenutzer =
                authenticatedUser.get().orElse(null);

// Die Bibliothek arbeitet mit konto.User.
// Keine ungeprüften Benutzer-IDs verwenden.
        if (angemeldeterBenutzer instanceof User) {
            return (User) angemeldeterBenutzer;
        }

        return null;
    }


// =====================================================
// 2. ECHTE BÜCHER LADEN
// =====================================================

    private void ladeTagebuch() {

        buchListe.removeAll();

        User benutzer = ermittleBenutzer();

        if (benutzer == null) {

            buchListe.add(new Paragraph(
                    "Der angemeldete Benutzer konnte noch " +
                            "nicht mit dem Bibliotheksbenutzer " +
                            "verknüpft werden."
            ));

            return;
        }

        Long benutzerId = benutzer.getId();

        if (benutzerId == null) {

            buchListe.add(new Paragraph(
                    "Keine gültige Benutzer-ID vorhanden."
            ));

            return;
        }

// Alle Bücher mit Status Lese_ich_gerade
        List<Bibliothekseintrag> aktuelleBuecher =
                bibliothekRepository
                        .findByBenutzerAndLesestatusOrderByReadlistPositionAsc(
                                benutzer,
                                Lesestatus.Lese_ich_gerade
                        );

        int anzahl = 0;

        for (Bibliothekseintrag eintrag : aktuelleBuecher) {

            Buch buch = eintrag.getBuch();

            if (buch == null || buch.getId() == null) {
                continue;
            }

            int geleseneSeiten =
                    tagebuchService
                            .berechneGesamtseitenFuerBuch(
                                    benutzerId,
                                    buch.getId()
                            );

            buchListe.add(
                    erstelleBuchKarte(
                            buch,
                            geleseneSeiten
                    )
            );

            anzahl++;
        }

        aktiveBuecher.setText(
                String.valueOf(anzahl)
        );

// Tagesseiten aller Bücher zusammen
        int seitenHeute =
                tagebuchService
                        .berechneTagesseiten(benutzerId);

        heuteGesamt.setText(
                String.valueOf(seitenHeute)
        );

        if (anzahl == 0) {

            buchListe.add(new Paragraph(
                    "Du liest aktuell noch kein Buch. " +
                            "Wähle in Meine Bibliothek ein Buch " +
                            "mit dem Status 'Lese ich gerade'."
            ));
        }
    }


// =====================================================
// 3. KLICKBARE BUCHKARTE
// =====================================================

    private Div erstelleBuchKarte(
            Buch buch,
            int geleseneSeiten) {

        Div karte = erstelleKarte();

        karte.getStyle()
                .set("cursor", "pointer");

        HorizontalLayout inhalt =
                new HorizontalLayout();

        inhalt.setWidthFull();
        inhalt.setAlignItems(Alignment.CENTER);

// Buchsymbol
        Div cover = new Div();
        cover.setText("📖");

        cover.getStyle()
                .set("background", "var(--readly-hintergrund)")
                .set("border-radius", "8px")
                .set("width", "65px")
                .set("height", "90px")
                .set("min-width", "65px")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("font-size", "26px");

        VerticalLayout information =
                new VerticalLayout();

        information.setPadding(false);
        information.setSpacing(false);
        information.setWidthFull();

// Buchtitel
        H2 buchTitel = new H2(
                buch.getTitel() == null
                        ? "Ohne Titel"
                        : buch.getTitel()
        );

        buchTitel.getStyle()
                .set("margin", "0")
                .set("font-size", "1.25rem")
                .set(
                        "font-family",
                        "var(--readly-schrift-ueberschrift)"
                );

        Span status = new Span("Lese ich gerade");

        status.getStyle()
                .set("color", "var(--readly-text-sekundaer)")
                .set("font-size", "14px");

        information.add(buchTitel, status);

        Integer gesamtseiten = buch.getSeitenzahl();

        if (gesamtseiten != null && gesamtseiten > 0) {

            double prozent = Math.min(
                    100,
                    100.0 * geleseneSeiten / gesamtseiten
            );

            Span seitenText = new Span(
                    geleseneSeiten + " / " +
                            gesamtseiten + " Seiten · " +
                            Math.round(prozent) + " %"
            );

            Div balken = new Div();

            balken.getStyle()
                    .set("height", "7px")
                    .set("width", "100%")
                    .set("background", "var(--readly-rahmen)")
                    .set("border-radius", "8px")
                    .set("overflow", "hidden")
                    .set("margin-top", "8px");

            Div fuellung = new Div();

            fuellung.getStyle()
                    .set("height", "100%")
                    .set("width", prozent + "%")
                    .set("background", "var(--readly-primaer)");

            balken.add(fuellung);

            information.add(seitenText, balken);

        } else {

            information.add(new Span(
                    geleseneSeiten +
                            " Seiten dokumentiert"
            ));
        }

        Span pfeil = new Span("›");

        pfeil.getStyle()
                .set("font-size", "28px")
                .set("color", "var(--readly-primaer)");

        inhalt.add(cover, information, pfeil);
        inhalt.setFlexGrow(1, information);

        karte.add(inhalt);

// Auf die Detailseite des Buches wechseln
        karte.addClickListener(event ->

                UI.getCurrent().navigate(
                        "tagebuch/buch/" + buch.getId()
                )
        );

        return karte;
    }


// =====================================================
// 4. STATISTIKKARTE
// =====================================================

    private Div erstelleStatistikKarte(
            String bezeichnung,
            Span wert,
            String einheit) {

        Div karte = erstelleKarte();

        karte.getStyle()
                .set("flex", "1");

        Span label = new Span(bezeichnung);

        label.getStyle()
                .set("display", "block")
                .set("color", "var(--readly-text-sekundaer)");

        wert.getStyle()
                .set("font-size", "30px")
                .set("font-weight", "600")
                .set("color", "var(--readly-text)");

        Span einheitText = new Span(
                " " + einheit
        );

        karte.add(
                label,
                wert,
                einheitText
        );

        return karte;
    }


// =====================================================
// 5. READLY-DESIGN
// =====================================================

    private Div erstelleKarte() {

        Div karte = new Div();

        karte.setWidthFull();

        karte.getStyle()
                .set("background", "var(--readly-flaeche)")
                .set(
                        "border",
                        "1px solid var(--readly-rahmen)"
                )
                .set("border-radius", "16px")
                .set("padding", "20px")
                .set("box-sizing", "border-box");

        return karte;
    }
}