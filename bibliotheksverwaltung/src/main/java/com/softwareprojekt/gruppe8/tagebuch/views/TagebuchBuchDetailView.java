package com.softwareprojekt.gruppe8.tagebuch.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;
import com.softwareprojekt.gruppe8.security.AuthenticatedUser;

import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;

import com.softwareprojekt.gruppe8.bibliothek.entity.Bibliothekseintrag;
import com.softwareprojekt.gruppe8.bibliothek.repository.BibliothekseintragRepository;

import com.softwareprojekt.gruppe8.tagebuch.domain.Tagebucheintrag;
import com.softwareprojekt.gruppe8.tagebuch.service.TagebuchService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Route(
        value = "tagebuch/buch",
        layout = MainLayout.class
)
@PermitAll
public class TagebuchBuchDetailView
        extends VerticalLayout
        implements HasUrlParameter<Long> {

// =====================================================
// SERVICES UND REPOSITORIES
// =====================================================

    private final TagebuchService tagebuchService;

    private final BibliothekseintragRepository bibliothekRepository;

    private final AuthenticatedUser authenticatedUser;

    private Long buchId;

    private static final DateTimeFormatter DATUM_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

// =====================================================
// OBERFLÄCHE
// =====================================================

    private final H1 buchTitel = new H1("Buchdetails");

    private final Span buchInformation =
            new Span("Buchinformationen werden geladen.");

    private final Span fortschrittText = new Span("—");

    private final Span fortschrittProzent = new Span("—");

    private final Div fortschrittBalken = new Div();

    private final Span heuteGelesen = new Span("—");

    private final Span restlicheSeiten = new Span("—");

    private final DatePicker datum =
            new DatePicker("Datum");

    private final IntegerField geleseneSeiten =
            new IntegerField("Zusätzlich gelesene Seiten");

    private final TextArea notiz =
            new TextArea("Meine Notiz");

    private final Button speichern =
            new Button("Eintrag hinzufügen");

    private final VerticalLayout eintragsListe =
            new VerticalLayout();

// =====================================================
// KONSTRUKTOR
// =====================================================

    public TagebuchBuchDetailView(
            TagebuchService tagebuchService,
            BibliothekseintragRepository bibliothekRepository,
            AuthenticatedUser authenticatedUser) {

        this.tagebuchService = tagebuchService;
        this.bibliothekRepository = bibliothekRepository;
        this.authenticatedUser = authenticatedUser;

        setWidthFull();
        setMaxWidth("1000px");
        setPadding(true);
        setSpacing(true);

        getStyle().set("margin", "0 auto");

// Zurück zur Übersicht
        Button zurueck = new Button(
                "← Zurück zum Lesetagebuch",
                event -> getUI().ifPresent(ui ->
                        ui.navigate(TagebuchView.class)
                )
        );

        zurueck.getStyle()
                .set("color", "var(--readly-primaer)");

        buchTitel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)")
                .set("color", "var(--readly-text)")
                .set("margin-bottom", "0");

        buchInformation.getStyle()
                .set("color", "var(--readly-text-sekundaer)");

// =================================================
// 1. BUCHINFORMATIONEN
// =================================================

        Div buchKarte = erstelleKarte();

        Span buchSymbol = new Span("📖");

        buchSymbol.getStyle()
                .set("font-size", "40px")
                .set("display", "block")
                .set("margin-bottom", "12px");

        H2 fortschrittTitel = new H2("Mein Lesefortschritt");

        fortschrittTitel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)");

        fortschrittText.getStyle()
                .set("font-size", "18px")
                .set("font-weight", "600");

        fortschrittProzent.getStyle()
                .set("color", "var(--readly-text-sekundaer)");

        Div balkenHintergrund = new Div();

        balkenHintergrund.getStyle()
                .set("width", "100%")
                .set("height", "9px")
                .set("background", "var(--readly-rahmen)")
                .set("border-radius", "8px")
                .set("overflow", "hidden")
                .set("margin-top", "12px")
                .set("margin-bottom", "8px");

        fortschrittBalken.getStyle()
                .set("height", "100%")
                .set("width", "0%")
                .set("background", "var(--readly-primaer)")
                .set("border-radius", "8px");

        balkenHintergrund.add(fortschrittBalken);

        buchKarte.add(
                buchSymbol,
                fortschrittTitel,
                fortschrittText,
                balkenHintergrund,
                fortschrittProzent
        );

// =================================================
// 2. STATISTIK
// =================================================

        HorizontalLayout statistik = new HorizontalLayout();
        statistik.setWidthFull();

        Div heuteKarte = erstelleStatistikKarte(
                "Heute gelesen",
                heuteGelesen,
                "Seiten"
        );

        Div restKarte = erstelleStatistikKarte(
                "Noch zu lesen",
                restlicheSeiten,
                "Seiten"
        );

        statistik.add(heuteKarte, restKarte);
        statistik.setFlexGrow(1, heuteKarte, restKarte);

// =================================================
// 3. LESEFORTSCHRITT EINTRAGEN
// =================================================

        Div formularKarte = erstelleKarte();

        H2 formularTitel = new H2(
                "Lesefortschritt eintragen"
        );

        formularTitel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)");

        Paragraph formularBeschreibung = new Paragraph(
                "Du kannst mehrmals am Tag Seiten eintragen. " +
                        "Alle Einträge werden später automatisch addiert."
        );

        formularBeschreibung.getStyle()
                .set("color", "var(--readly-text-sekundaer)");

        datum.setValue(LocalDate.now());
        datum.setMax(LocalDate.now());
        datum.setWidthFull();
        datum.setRequiredIndicatorVisible(true);

        geleseneSeiten.setMin(1);
        geleseneSeiten.setStepButtonsVisible(true);
        geleseneSeiten.setWidthFull();
        geleseneSeiten.setRequiredIndicatorVisible(true);

        notiz.setWidthFull();
        notiz.setMaxLength(2000);
        notiz.setPlaceholder(
                "Was hat dir heute besonders gefallen?"
        );

        speichern.getStyle()
                .set("background", "var(--readly-primaer)")
                .set("color", "white")
                .set("border-radius", "8px");

// Noch keine Schreibzugriffe auf die gemeinsame DB.
        speichern.setEnabled(false);

        Paragraph hinweis = new Paragraph(
                "Das Speichern wird nach der Prüfung " +
                        "der Benutzerzuordnung und Datenbank aktiviert."
        );

        hinweis.getStyle()
                .set("font-size", "0.875rem")
                .set("color", "var(--readly-text-sekundaer)");

        formularKarte.add(
                formularTitel,
                formularBeschreibung,
                datum,
                geleseneSeiten,
                notiz,
                speichern,
                hinweis
        );

// =================================================
// 4. LESEEINTRÄGE
// =================================================

        Div verlaufKarte = erstelleKarte();

        H2 verlaufTitel = new H2("Meine Leseeinträge");

        verlaufTitel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)");

        eintragsListe.setWidthFull();
        eintragsListe.setPadding(false);
        eintragsListe.setSpacing(true);

        verlaufKarte.add(
                verlaufTitel,
                eintragsListe
        );

// =================================================
// ALLE BEREICHE HINZUFÜGEN
// =================================================

        add(
                zurueck,
                buchTitel,
                buchInformation,
                buchKarte,
                statistik,
                formularKarte,
                verlaufKarte
        );
    }

// =====================================================
// 5. BUCH-ID AUS URL
// =====================================================

    @Override
    public void setParameter(
            BeforeEvent event,
            Long parameter) {

        if (parameter == null || parameter <= 0) {
            event.rerouteTo(TagebuchView.class);
            return;
        }

        this.buchId = parameter;

        ladeBuchdetails();
    }

// =====================================================
// 6. ANGEMELDETEN BENUTZER ERMITTELN
// =====================================================

    private User ermittleBenutzer() {

        Object angemeldeterBenutzer =
                authenticatedUser.get().orElse(null);

        if (angemeldeterBenutzer instanceof User user) {
            return user;
        }

        return null;
    }

// =====================================================
// 7. BUCHDETAILS LADEN
// =====================================================

    private void ladeBuchdetails() {

        User benutzer = ermittleBenutzer();

        if (benutzer == null || benutzer.getId() == null) {
            zeigeFehler(
                    "Der angemeldete Benutzer konnte " +
                            "nicht zugeordnet werden."
            );
            return;
        }

// Nur Bücher aus der eigenen Bibliothek prüfen.
        List<Bibliothekseintrag> bibliothek =
                bibliothekRepository.findByBenutzer(benutzer);

        Buch buch = bibliothek.stream()
                .map(Bibliothekseintrag::getBuch)
                .filter(Objects::nonNull)
                .filter(b ->
                        buchId.equals(b.getId())
                )
                .findFirst()
                .orElse(null);

// Kein Zugriff auf Bücher anderer Benutzer.
        if (buch == null) {
            zeigeFehler(
                    "Dieses Buch befindet sich nicht " +
                            "in deiner Bibliothek."
            );
            return;
        }

        buchTitel.setText(
                buch.getTitel() == null
                        ? "Ohne Titel"
                        : buch.getTitel()
        );

        Integer gesamtseiten = buch.getSeitenzahl();

        buchInformation.setText(
                gesamtseiten == null
                        ? "Gesamtseitenzahl nicht verfügbar"
                        : "Buch mit " + gesamtseiten + " Seiten"
        );

        Long benutzerId = benutzer.getId();

// Dokumentierte Seiten für dieses Buch
        int dokumentierteSeiten =
                tagebuchService.berechneGesamtseitenFuerBuch(
                        benutzerId,
                        buchId
                );

// Heute gelesene Seiten für dieses Buch
        int seitenHeute =
                tagebuchService.berechneTagesseitenFuerBuch(
                        benutzerId,
                        buchId
                );

        heuteGelesen.setText(
                String.valueOf(seitenHeute)
        );

        aktualisiereFortschritt(
                dokumentierteSeiten,
                gesamtseiten
        );

// Nur die Einträge dieses Benutzers und Buches.
        List<Tagebucheintrag> eintraege =
                tagebuchService.findeEintraegeFuerBuch(
                        benutzerId,
                        buchId
                );

        zeigeEintraege(eintraege);
    }

// =====================================================
// 8. FORTSCHRITT AKTUALISIEREN
// =====================================================

    private void aktualisiereFortschritt(
            int dokumentierteSeiten,
            Integer gesamtseiten) {

        if (gesamtseiten == null || gesamtseiten <= 0) {

            fortschrittText.setText(
                    dokumentierteSeiten +
                            " Seiten dokumentiert"
            );

            fortschrittProzent.setText(
                    "Gesamtseitenzahl unbekannt"
            );

            fortschrittBalken.getStyle()
                    .set("width", "0%");

            restlicheSeiten.setText("—");

            return;
        }

        double prozent = Math.min(
                100,
                100.0 * dokumentierteSeiten / gesamtseiten
        );

        fortschrittText.setText(
                dokumentierteSeiten +
                        " / " + gesamtseiten + " Seiten"
        );

        fortschrittProzent.setText(
                Math.round(prozent) + " % dokumentiert"
        );

        fortschrittBalken.getStyle()
                .set("width", prozent + "%");

        int rest = Math.max(
                0,
                gesamtseiten - dokumentierteSeiten
        );

        restlicheSeiten.setText(
                String.valueOf(rest)
        );
    }

// =====================================================
// 9. LESEEINTRÄGE ANZEIGEN
// =====================================================

    private void zeigeEintraege(
            List<Tagebucheintrag> eintraege) {

        eintragsListe.removeAll();

        if (eintraege.isEmpty()) {

            eintragsListe.add(new Paragraph(
                    "Für dieses Buch gibt es noch keine Einträge."
            ));

            return;
        }

        for (Tagebucheintrag eintrag : eintraege) {

            Div karte = erstelleKarte();

            String datumText =
                    eintrag.getDatum() == null
                            ? "Ohne Datum"
                            : eintrag.getDatum()
                            .format(DATUM_FORMAT);

            int seiten =
                    eintrag.getGeleseneSeiten() == null
                            ? 0
                            : eintrag.getGeleseneSeiten();

            Span datumAnzeige = new Span(datumText);

            datumAnzeige.getStyle()
                    .set("color",
                            "var(--readly-text-sekundaer)");

            Span seitenAnzeige = new Span(
                    "+" + seiten + " Seiten"
            );

            seitenAnzeige.getStyle()
                    .set("font-weight", "600")
                    .set("color", "var(--readly-primaer)");

            karte.add(
                    datumAnzeige,
                    new Div(),
                    seitenAnzeige
            );

            String eintragNotiz = eintrag.getNotiz();

            if (eintragNotiz != null
                    && !eintragNotiz.isBlank()) {

                Paragraph notizAnzeige =
                        new Paragraph(eintragNotiz);

                karte.add(notizAnzeige);
            }

            eintragsListe.add(karte);
        }
    }

// =====================================================
// 10. FEHLERMELDUNG
// =====================================================

    private void zeigeFehler(String meldung) {

        buchTitel.setText("Buch nicht verfügbar");
        buchInformation.setText(meldung);

        fortschrittText.setText("—");
        fortschrittProzent.setText("—");

        fortschrittBalken.getStyle()
                .set("width", "0%");

        heuteGelesen.setText("—");
        restlicheSeiten.setText("—");

        eintragsListe.removeAll();

        eintragsListe.add(new Paragraph(meldung));

        speichern.setEnabled(false);

        Notification.show(meldung);
    }

// =====================================================
// 11. STATISTIKKARTE
// =====================================================

    private Div erstelleStatistikKarte(
            String bezeichnung,
            Span wert,
            String einheit) {

        Div karte = erstelleKarte();

        karte.getStyle().set("flex", "1");

        Span label = new Span(bezeichnung);

        label.getStyle()
                .set("display", "block")
                .set("color",
                        "var(--readly-text-sekundaer)");

        wert.getStyle()
                .set("font-size", "28px")
                .set("font-weight", "600")
                .set("color", "var(--readly-text)");

        karte.add(
                label,
                wert,
                new Span(" " + einheit)
        );

        return karte;
    }

// =====================================================
// 12. READLY-DESIGN
// =====================================================

    private Div erstelleKarte() {

        Div karte = new Div();
        karte.setWidthFull();

        karte.getStyle()
                .set("background", "var(--readly-flaeche)")
                .set("border",
                        "1px solid var(--readly-rahmen)")
                .set("border-radius", "16px")
                .set("padding", "22px")
                .set("box-sizing", "border-box");

        return karte;
    }
}