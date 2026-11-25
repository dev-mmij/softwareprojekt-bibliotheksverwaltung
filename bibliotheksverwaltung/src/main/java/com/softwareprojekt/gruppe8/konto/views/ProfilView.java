package com.softwareprojekt.gruppe8.konto.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "mein-konto", layout = MainLayout.class)
@Menu(title = "Mein Konto", order = 500)
@PermitAll
public class ProfilView extends VerticalLayout {

    private final String burgunder = "#7A2E3A";
    private final String text = "var(--readly-text)";
    private final String sekundaer = "var(--readly-text-sekundaer)";
    private final String flaeche = "var(--readly-flaeche)";
    private final String rahmen = "var(--readly-rahmen)";

    private TextField vorname;
    private TextField nachname;
    private TextField anzeigename;
    private TextField standort;
    private TextArea bio;

    public ProfilView() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);

        getStyle()
                .set("max-width", "1200px")
                .set("margin", "0 auto")
                .set("box-sizing", "border-box")
                .set("background", "#F7F0E7");

        add(
                erstelleKopfbereich(),
                erstelleProfilbereich()
        );
    }

    private Component erstelleKopfbereich() {
        H1 titel = new H1("Mein Profil");
        titel.getStyle()
                .set("font-family", "var(--readly-schrift-ueberschrift)")
                .set("color", text)
                .set("margin", "0");

        Paragraph beschreibung = new Paragraph(
                "Ein paar Zeilen über dich und deine Liebe zu Büchern."
        );
        beschreibung.getStyle()
                .set("color", sekundaer)
                .set("margin", "0");

        VerticalLayout bereich = new VerticalLayout(
                titel,
                beschreibung
        );

        bereich.setPadding(false);
        bereich.setSpacing(false);

        return bereich;
    }

    private Component erstelleProfilbereich() {
        HorizontalLayout bereich = new HorizontalLayout(
                erstellePersoenlicheAngaben(),
                erstelleProfilkarte()
        );

        bereich.setWidthFull();
        bereich.setAlignItems(Alignment.START);
        bereich.setSpacing(true);

        return bereich;
    }

    private VerticalLayout erstellePersoenlicheAngaben() {
        H2 titel = new H2("Persönliche Angaben");
        titel.getStyle()
                .set("font-family", "var(--readly-schrift-ueberschrift)")
                .set("font-size", "1.15rem")
                .set("margin", "0")
                .set("color", text);

        vorname = erstelleTextfeld("Vorname", "Lena");
        nachname = erstelleTextfeld("Nachname", "Meyer");

        HorizontalLayout namen = new HorizontalLayout(
                vorname,
                nachname
        );

        namen.setWidthFull();
        namen.setSpacing(true);
        namen.setFlexGrow(1, vorname);
        namen.setFlexGrow(1, nachname);

        anzeigename = erstelleTextfeld("Anzeigename", "Lena liest");
        standort = erstelleTextfeld("Standort", "Leipzig, Deutschland");

        bio = new TextArea("Kurze Bio");
        bio.setValue(
                "Am liebsten zwischen alten Buchseiten und neuen Geschichten.\n" +
                        "Ich lese Gegenwartsliteratur und entdecke kleine Verlage."
        );
        bio.setWidthFull();
        bio.setMinHeight("90px");
        styleTextField(bio);

        Button zuruecksetzen = new Button("Zurücksetzen");
        zuruecksetzen.getStyle()
                .set("color", burgunder)
                .set("border", "1px solid " + rahmen)
                .set("background", flaeche);

        zuruecksetzen.addClickListener(event -> setStandardwerte());

        Button speichern = new Button("Änderungen speichern");
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        speichern.getStyle()
                .set("background", burgunder)
                .set("color", "white");

        HorizontalLayout aktionen = new HorizontalLayout(
                zuruecksetzen,
                speichern
        );

        aktionen.setWidthFull();
        aktionen.setJustifyContentMode(JustifyContentMode.END);
        aktionen.getStyle()
                .set("border-top", "1px solid " + rahmen)
                .set("padding-top", "16px");

        VerticalLayout karte = new VerticalLayout(
                titel,
                namen,
                anzeigename,
                standort,
                bio,
                aktionen
        );

        karte.setPadding(true);
        karte.setSpacing(true);
        karte.setWidthFull();
        karte.setMaxWidth("680px");

        styleKarte(karte);

        return karte;
    }

    private VerticalLayout erstelleProfilkarte() {
        Span kuerzel = new Span("LM");
        kuerzel.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("width", "42px")
                .set("height", "42px")
                .set("border-radius", "50%")
                .set("background", "#F1E3E0")
                .set("color", burgunder)
                .set("font-weight", "600");

        H3 name = new H3("Lena liest");
        name.getStyle()
                .set("font-family", "var(--readly-schrift-ueberschrift)")
                .set("font-size", "1.15rem")
                .set("margin", "12px 0 0")
                .set("color", text);

        Span benutzername = new Span("Lena Meyer");
        benutzername.getStyle()
                .set("color", sekundaer)
                .set("font-size", "0.85rem");

        HorizontalLayout ort = new HorizontalLayout(
                new Icon(VaadinIcon.MAP_MARKER),
                new Span("Leipzig, Deutschland")
        );

        ort.setAlignItems(Alignment.CENTER);
        ort.getStyle()
                .set("color", sekundaer)
                .set("font-size", "0.8rem")
                .set("margin-top", "12px");

        Div trennlinie = new Div();
        trennlinie.getStyle()
                .set("height", "1px")
                .set("background", rahmen)
                .set("margin", "14px 0");

        HorizontalLayout profilIdTitel = new HorizontalLayout(
                new Span("Profil-ID"),
                new Icon(VaadinIcon.LOCK)
        );

        profilIdTitel.setWidthFull();
        profilIdTitel.setJustifyContentMode(JustifyContentMode.BETWEEN);
        profilIdTitel.getStyle()
                .set("font-size", "0.75rem")
                .set("color", sekundaer);

        TextField profilId = new TextField();
        profilId.setValue("RD-004821");
        profilId.setReadOnly(true);
        profilId.setWidthFull();
        styleTextField(profilId);

        Paragraph hinweis = new Paragraph(
                "Deine Profil-ID wird automatisch vergeben und kann nicht geändert werden."
        );
        hinweis.getStyle()
                .set("font-size", "0.75rem")
                .set("line-height", "1.4")
                .set("color", sekundaer)
                .set("margin", "8px 0 0");

        VerticalLayout karte = new VerticalLayout(
                kuerzel,
                name,
                benutzername,
                ort,
                trennlinie,
                profilIdTitel,
                profilId,
                hinweis
        );

        karte.setPadding(true);
        karte.setSpacing(false);
        karte.setWidth("280px");
        karte.setMinWidth("280px");

        styleKarte(karte);

        return karte;
    }

    private TextField erstelleTextfeld(String beschriftung, String wert) {
        TextField feld = new TextField(beschriftung);
        feld.setValue(wert);
        feld.setWidthFull();
        styleTextField(feld);
        return feld;
    }

    private void styleTextField(Component feld) {
        feld.getStyle()
                .set("--lumo-border-radius-m", "6px")
                .set("--lumo-contrast-10pct", rahmen)
                .set("--lumo-primary-color", burgunder);
    }

    private void styleKarte(Component karte) {
        karte.getStyle()
                .set("background", flaeche)
                .set("border", "1px solid " + rahmen)
                .set("border-radius", "8px")
                .set("box-sizing", "border-box");
    }

    private void setStandardwerte() {
        vorname.setValue("Lena");
        nachname.setValue("Meyer");
        anzeigename.setValue("Lena liest");
        standort.setValue("Leipzig, Deutschland");
        bio.setValue(
                "Am liebsten zwischen alten Buchseiten und neuen Geschichten.\n" +
                        "Ich lese Gegenwartsliteratur und entdecke kleine Verlage."
        );
    }
}
