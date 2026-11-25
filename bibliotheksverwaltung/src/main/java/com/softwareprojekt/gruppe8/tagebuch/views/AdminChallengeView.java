package com.softwareprojekt.gruppe8.tagebuch.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;
import com.softwareprojekt.gruppe8.tagebuch.domain.ChallengeTyp;
import com.softwareprojekt.gruppe8.tagebuch.domain.Lesechallenge;
import com.softwareprojekt.gruppe8.tagebuch.service.ChallengeService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "admin/challenges/new", layout = MainLayout.class)
@Menu(title = "Challenge erstellen", order = 910)
@RolesAllowed("ADMIN")
public class AdminChallengeView extends VerticalLayout {

    private final ChallengeService challengeService;

    // Bis zur Freigabe der Datenbankmigration deaktiviert.
    private static final boolean SPEICHERN_AKTIVIERT = false;

    // Deaktivieren ebenfalls erst nach Datenbankfreigabe.
    private static final boolean DEAKTIVIEREN_AKTIVIERT = false;

    private static final DateTimeFormatter DATUM_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // Formularfelder
    private final Select<ChallengeTyp> typ = new Select<>();

    private final TextField challengeTitel =
            new TextField("Titel");

    private final TextArea challengeBeschreibung =
            new TextArea("Beschreibung");

    private final DatePicker startdatum =
            new DatePicker("Startdatum");

    private final DatePicker enddatum =
            new DatePicker("Enddatum");

    private final IntegerField zielWert =
            new IntegerField("Zielwert");

    private final IntegerField belohnungPunkte =
            new IntegerField("Belohnungspunkte");

    private final Select<String> abzeichenName =
            new Select<>();

    // Tabelle für die Challenge-Verwaltung
    private final Grid<Lesechallenge> challengeGrid =
            new Grid<>(Lesechallenge.class, false);


// =====================================================
// KONSTRUKTOR
// =====================================================

    public AdminChallengeView(ChallengeService challengeService) {

        this.challengeService = challengeService;

        setWidthFull();
        setMaxWidth("1100px");
        setPadding(true);
        setSpacing(true);
        getStyle().set("margin", "0 auto");

        H1 titel = new H1("Neue Challenge erstellen");

        titel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)")
                .set("color", "var(--readly-text)")
                .set("margin-bottom", "0");

        Paragraph beschreibung = new Paragraph(
                "Erstelle neue Lesechallenges und " +
                        "verwalte bestehende Challenges."
        );

        beschreibung.getStyle()
                .set("color", "var(--readly-text-sekundaer)")
                .set("margin-top", "0");

        add(
                titel,
                beschreibung,
                erstelleFormularKarte(),
                erstelleVerwaltungsKarte()
        );
    }


// =====================================================
// 1. FORMULAR ZUM ERSTELLEN
// =====================================================

    private Div erstelleFormularKarte() {

        Div karte = erstelleKarte();

        typ.setLabel("Challenge-Typ");
        typ.setItems(ChallengeTyp.values());
        typ.setRequiredIndicatorVisible(true);

        typ.setItemLabelGenerator(wert -> switch (wert) {

            case JAHRESCHALLENGE -> "Jahreschallenge";

            case MONATSCHALLENGE -> "Monatschallenge";

            case TAEGLICHE_CHALLENGE -> "Tägliche Challenge";
        });

        challengeTitel.setRequiredIndicatorVisible(true);
        challengeTitel.setMaxLength(150);
        challengeTitel.setWidthFull();

        challengeBeschreibung.setWidthFull();
        challengeBeschreibung.setMaxLength(2000);

        startdatum.setRequiredIndicatorVisible(true);
        enddatum.setRequiredIndicatorVisible(true);

        zielWert.setRequiredIndicatorVisible(true);
        zielWert.setMin(1);
        zielWert.setStepButtonsVisible(true);

        belohnungPunkte.setRequiredIndicatorVisible(true);
        belohnungPunkte.setMin(0);
        belohnungPunkte.setStepButtonsVisible(true);
        belohnungPunkte.setValue(0);

        abzeichenName.setLabel("Abzeichen");

        abzeichenName.setItems(
                "Lesekönig",
                "Monatsheld",
                "Leseroutine"
        );

        abzeichenName.setPlaceholder(
                "Abzeichen auswählen"
        );

        abzeichenName.setWidthFull();

// Ziel und Abzeichen automatisch anpassen.
        typ.addValueChangeListener(event -> {

            ChallengeTyp auswahl = event.getValue();

            if (auswahl == null) {

                zielWert.setLabel("Zielwert");
                abzeichenName.clear();

            } else if (auswahl ==
                    ChallengeTyp.JAHRESCHALLENGE) {

                zielWert.setLabel("Anzahl Bücher");
                abzeichenName.setValue("Lesekönig");

            } else if (auswahl ==
                    ChallengeTyp.MONATSCHALLENGE) {

                zielWert.setLabel("Anzahl Bücher");
                abzeichenName.setValue("Monatsheld");

            } else if (auswahl ==
                    ChallengeTyp.TAEGLICHE_CHALLENGE) {

                zielWert.setLabel("Seiten pro Tag");
                abzeichenName.setValue("Leseroutine");
            }
        });

        FormLayout formular = new FormLayout();

        formular.add(
                typ,
                challengeTitel,
                challengeBeschreibung,
                startdatum,
                enddatum,
                zielWert,
                belohnungPunkte,
                abzeichenName
        );

        formular.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        formular.setColspan(challengeBeschreibung, 2);

        Button speichern = new Button(
                "Challenge erstellen",
                event -> speichereChallenge()
        );

        speichern.getStyle()
                .set("background", "var(--readly-primaer)")
                .set("color", "white")
                .set("border-radius", "8px");

        speichern.setEnabled(SPEICHERN_AKTIVIERT);

        Paragraph hinweis = new Paragraph(
                "Das Erstellen und Deaktivieren von Challenges " +
                        "wird nach der Datenbankfreigabe aktiviert."
        );

        hinweis.getStyle()
                .set("color", "var(--readly-text-sekundaer)")
                .set("font-size", "0.875rem");

        karte.add(formular, speichern, hinweis);

        return karte;
    }


// =====================================================
// 2. CHALLENGES VERWALTEN
// =====================================================

    private Div erstelleVerwaltungsKarte() {

        Div karte = erstelleKarte();

        H2 titel = new H2("Challenges verwalten");

        titel.getStyle()
                .set("font-family",
                        "var(--readly-schrift-ueberschrift)")
                .set("color", "var(--readly-text)");

        Paragraph beschreibung = new Paragraph(
                "Hier kannst du aktive Challenges ansehen " +
                        "und bei Bedarf deaktivieren."
        );

        beschreibung.getStyle()
                .set("color", "var(--readly-text-sekundaer)");

        challengeGrid.setWidthFull();
        challengeGrid.setAllRowsVisible(true);

// Titel
        challengeGrid
                .addColumn(Lesechallenge::getTitel)
                .setHeader("Titel")
                .setAutoWidth(true)
                .setFlexGrow(2);

// Challenge-Typ
        challengeGrid
                .addColumn(challenge -> {

                    if (challenge.getTyp() == null) {
                        return "—";
                    }

                    return switch (challenge.getTyp()) {

                        case JAHRESCHALLENGE ->
                                "Jahreschallenge";

                        case MONATSCHALLENGE ->
                                "Monatschallenge";

                        case TAEGLICHE_CHALLENGE ->
                                "Täglich";
                    };
                })
                .setHeader("Typ")
                .setAutoWidth(true);

// Zielwert
        challengeGrid
                .addColumn(challenge -> {

                    Integer ziel = challenge.getZielWert();

                    if (ziel == null) {
                        return "—";
                    }

                    if (challenge.getTyp() ==
                            ChallengeTyp.TAEGLICHE_CHALLENGE) {

                        return ziel + " Seiten/Tag";
                    }

                    return ziel + " Bücher";
                })
                .setHeader("Ziel")
                .setAutoWidth(true);

// Startdatum
        challengeGrid
                .addColumn(challenge ->
                        formatiereDatum(
                                challenge.getStartdatum()
                        ))
                .setHeader("Startdatum")
                .setAutoWidth(true);

// Enddatum
        challengeGrid
                .addColumn(challenge ->
                        formatiereDatum(
                                challenge.getEnddatum()
                        ))
                .setHeader("Enddatum")
                .setAutoWidth(true);

// Punkte
        challengeGrid
                .addColumn(Lesechallenge::getBelohnungPunkte)
                .setHeader("Punkte")
                .setAutoWidth(true);

// Aktion: Challenge deaktivieren
        challengeGrid.addComponentColumn(challenge -> {

            Button deaktivieren =
                    new Button("Deaktivieren");

            deaktivieren.getStyle()
                    .set("color", "#B42318")
                    .set("border", "1px solid #B42318")
                    .set("border-radius", "8px")
                    .set("background", "transparent");

            deaktivieren.setEnabled(
                    DEAKTIVIEREN_AKTIVIERT
            );

            deaktivieren.addClickListener(event ->
                    zeigeDeaktivierenDialog(challenge)
            );

            return deaktivieren;

        }).setHeader("Aktionen").setAutoWidth(true);

// Aktive Challenges laden
        ladeAktiveChallenges();

        Button aktualisieren = new Button(
                "Liste aktualisieren",
                event -> ladeAktiveChallenges()
        );

        aktualisieren.getStyle()
                .set("color", "var(--readly-primaer)");

        karte.add(
                titel,
                beschreibung,
                challengeGrid,
                aktualisieren
        );

        return karte;
    }


// =====================================================
// 3. AKTIVE CHALLENGES LADEN
// =====================================================

    private void ladeAktiveChallenges() {

        List<Lesechallenge> aktiveChallenges =
                challengeService.findeAlleChallenges()
                        .stream()
                        .filter(Lesechallenge::isAktiv)
                        .toList();

        challengeGrid.setItems(aktiveChallenges);
    }


// =====================================================
// 4. BESTÄTIGUNGSDIALOG
// =====================================================

    private void zeigeDeaktivierenDialog(
            Lesechallenge challenge) {

        if (!DEAKTIVIEREN_AKTIVIERT) {
            Notification.show(
                    "Deaktivieren ist noch nicht freigeschaltet."
            );
            return;
        }

        ConfirmDialog dialog = new ConfirmDialog();

        dialog.setHeader("Challenge deaktivieren?");

        dialog.setText(
                "Möchtest du die Challenge \"" +
                        challenge.getTitel() +
                        "\" wirklich deaktivieren? " +
                        "Die bisherigen Teilnahmen und " +
                        "Belohnungen bleiben erhalten."
        );

        dialog.setCancelable(true);
        dialog.setCancelText("Abbrechen");

        dialog.setConfirmText("Deaktivieren");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(event -> {

            try {

                challengeService.deaktiviereChallenge(
                        challenge.getId()
                );

                Notification.show(
                        "Challenge wurde deaktiviert."
                );

                ladeAktiveChallenges();

            } catch (RuntimeException exception) {

                Notification.show(
                        "Deaktivieren fehlgeschlagen: " +
                                exception.getMessage()
                );
            }
        });

        dialog.open();
    }


// =====================================================
// 5. NEUE CHALLENGE SPEICHERN
// =====================================================

    private void speichereChallenge() {

        if (!SPEICHERN_AKTIVIERT) {

            Notification.show(
                    "Speichern ist noch nicht freigeschaltet."
            );

            return;
        }

        if (typ.getValue() == null
                || challengeTitel.isEmpty()
                || challengeTitel.getValue().isBlank()
                || startdatum.isEmpty()
                || enddatum.isEmpty()
                || zielWert.isEmpty()
                || belohnungPunkte.isEmpty()) {

            Notification.show(
                    "Bitte alle Pflichtfelder ausfüllen."
            );

            return;
        }

        LocalDate start = startdatum.getValue();
        LocalDate ende = enddatum.getValue();

        if (ende.isBefore(start)) {

            Notification.show(
                    "Das Enddatum darf nicht vor " +
                            "dem Startdatum liegen."
            );

            return;
        }

        if (zielWert.getValue() <= 0) {

            Notification.show(
                    "Der Zielwert muss größer als null sein."
            );

            return;
        }

        if (belohnungPunkte.getValue() < 0) {

            Notification.show(
                    "Belohnungspunkte dürfen nicht negativ sein."
            );

            return;
        }

        try {

            challengeService.erstelleChallenge(
                    challengeTitel.getValue(),
                    challengeBeschreibung.getValue(),
                    typ.getValue(),
                    start,
                    ende,
                    zielWert.getValue(),
                    belohnungPunkte.getValue(),
                    abzeichenName.getValue()
            );

            Notification.show(
                    "Challenge wurde erfolgreich erstellt!"
            );

            formularZuruecksetzen();
            ladeAktiveChallenges();

        } catch (RuntimeException exception) {

            Notification.show(
                    "Erstellen fehlgeschlagen: " +
                            exception.getMessage()
            );
        }
    }


// =====================================================
// 6. FORMULAR ZURÜCKSETZEN
// =====================================================

    private void formularZuruecksetzen() {

        typ.clear();
        challengeTitel.clear();
        challengeBeschreibung.clear();

        startdatum.clear();
        enddatum.clear();

        zielWert.clear();

        belohnungPunkte.setValue(0);
        abzeichenName.clear();
    }


// =====================================================
// 7. DATUM FORMATIEREN
// =====================================================

    private String formatiereDatum(LocalDate datum) {

        if (datum == null) {
            return "—";
        }

        return datum.format(DATUM_FORMAT);
    }


// =====================================================
// 8. READLY-DESIGN
// =====================================================

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
