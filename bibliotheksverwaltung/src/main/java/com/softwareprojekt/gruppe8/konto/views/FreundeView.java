package com.softwareprojekt.gruppe8.konto.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "freunde", layout = MainLayout.class)
@Menu(title = "Freunde", order = 510)
@PermitAll
public class FreundeView extends VerticalLayout {

    private final String burgunder = "#7A2E3A";
    private final String text = "var(--readly-text)";
    private final String sekundaer = "var(--readly-text-sekundaer)";
    private final String flaeche = "var(--readly-flaeche)";
    private final String rahmen = "var(--readly-rahmen)";

    public FreundeView() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);

        getStyle()
                .set("max-width", "1200px")
                .set("margin", "0 auto")
                .set("box-sizing", "border-box");

        add(
                erstelleKopfbereich(),
                erstelleAnfragen(),
                erstelleHauptbereich()
        );
    }

    private Component erstelleKopfbereich() {
        H1 titel = new H1("Freunde");
        titel.getStyle()
                .set("font-family", "var(--readly-schrift-ueberschrift)")
                .set("color", text)
                .set("margin", "0");

        Paragraph beschreibung = new Paragraph(
                "Verbinde dich mit anderen und tausche dich über Bücher aus."
        );
        beschreibung.getStyle()
                .set("color", sekundaer)
                .set("margin", "0");

        VerticalLayout ueberschrift = new VerticalLayout(
                titel,
                beschreibung
        );
        ueberschrift.setPadding(false);
        ueberschrift.setSpacing(false);

        TextField email = new TextField("Freunde per E-Mail einladen");
        email.setPlaceholder("E-Mail-Adresse");
        email.setWidth("230px");

        Button einladen = new Button("Einladen");
        einladen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        einladen.getStyle().set("background", burgunder);

        HorizontalLayout einladung = new HorizontalLayout(
                email,
                einladen
        );
        einladung.setAlignItems(Alignment.END);

        HorizontalLayout kopf = new HorizontalLayout(
                ueberschrift,
                einladung
        );

        kopf.setWidthFull();
        kopf.setJustifyContentMode(JustifyContentMode.BETWEEN);
        kopf.setAlignItems(Alignment.START);

        return kopf;
    }

    private Component erstelleAnfragen() {
        H3 titel = new H3("Freundschaftsanfragen");
        titel.getStyle()
                .set("font-family", "var(--readly-schrift-ueberschrift)")
                .set("margin", "0");

        Div leererZustand = new Div();
        leererZustand.setText("Keine neuen Freundschaftsanfragen.");
        leererZustand.getStyle()
                .set("color", sekundaer)
                .set("padding", "16px");

        Div karte = erstelleKarte();
        karte.add(leererZustand);

        HorizontalLayout karten = new HorizontalLayout(karte);
        karten.setWidthFull();

        VerticalLayout bereich = new VerticalLayout(
                titel,
                karten
        );

        bereich.setPadding(false);
        bereich.setSpacing(true);
        bereich.setWidthFull();

        return bereich;
    }

    private Component erstelleHauptbereich() {
        VerticalLayout freundeliste = erstelleFreundeliste();
        VerticalLayout chat = erstelleChat();

        HorizontalLayout bereich = new HorizontalLayout(
                freundeliste,
                chat
        );

        bereich.setWidthFull();
        bereich.setAlignItems(Alignment.STRETCH);
        bereich.setFlexGrow(0, freundeliste);
        bereich.setFlexGrow(1, chat);

        return bereich;
    }

    private VerticalLayout erstelleFreundeliste() {
        H3 titel = new H3("Meine Freunde");
        titel.getStyle()
                .set("font-family", "var(--readly-schrift-ueberschrift)")
                .set("margin", "0");

        Span anzahl = new Span("0 Freunde");
        anzahl.getStyle()
                .set("font-size", "0.8rem")
                .set("color", sekundaer);

        HorizontalLayout kopf = new HorizontalLayout(
                titel,
                anzahl
        );

        kopf.setWidthFull();
        kopf.setJustifyContentMode(JustifyContentMode.BETWEEN);
        kopf.setAlignItems(Alignment.BASELINE);

        TextField suche = new TextField();
        suche.setPlaceholder("Freunde suchen");
        suche.setWidthFull();

        Button alle = new Button("Alle");
        Button online = new Button("Online");

        HorizontalLayout tabs = new HorizontalLayout(
                alle,
                online
        );
        tabs.setSpacing(false);

        Div leererZustand = new Div();
        leererZustand.setText("Noch keine Freunde vorhanden.");
        leererZustand.getStyle()
                .set("color", sekundaer)
                .set("padding", "20px 8px")
                .set("text-align", "center");

        VerticalLayout personen = new VerticalLayout(leererZustand);
        personen.setPadding(false);
        personen.setSpacing(false);

        Span seitenzahl = new Span("Keine Einträge");
        seitenzahl.getStyle()
                .set("font-size", "0.8rem")
                .set("color", sekundaer);

        VerticalLayout liste = new VerticalLayout(
                kopf,
                suche,
                tabs,
                personen,
                seitenzahl
        );

        liste.setPadding(true);
        liste.setSpacing(true);
        liste.setWidth("280px");
        liste.setMinWidth("280px");

        liste.getStyle()
                .set("background", flaeche)
                .set("border", "1px solid " + rahmen)
                .set("border-radius", "16px")
                .set("box-sizing", "border-box");

        return liste;
    }

    private VerticalLayout erstelleChat() {
        Span titel = new Span("Nachrichten");
        titel.getStyle()
                .set("font-family", "var(--readly-schrift-ueberschrift)")
                .set("font-size", "1.2rem")
                .set("font-weight", "600")
                .set("color", text);

        Button optionen = new Button("...");

        HorizontalLayout kopf = new HorizontalLayout(
                titel,
                optionen
        );

        kopf.setWidthFull();
        kopf.setJustifyContentMode(JustifyContentMode.BETWEEN);
        kopf.setAlignItems(Alignment.CENTER);

        HorizontalLayout aktionen = new HorizontalLayout(
                new Button("Profil ansehen"),
                new Button("Stummschalten"),
                new Button("Entfernen")
        );

        Div leererChat = new Div();
        leererChat.setText(
                "Wähle einen Freund aus, um Nachrichten anzuzeigen."
        );

        leererChat.getStyle()
                .set("color", sekundaer)
                .set("text-align", "center")
                .set("padding", "100px 20px")
                .set("width", "100%")
                .set("box-sizing", "border-box");

        TextField nachricht = new TextField();
        nachricht.setPlaceholder("Nachricht schreiben ...");
        nachricht.setWidthFull();

        Button senden = new Button("Senden");
        senden.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        senden.getStyle().set("background", burgunder);
        senden.setEnabled(false);

        HorizontalLayout eingabe = new HorizontalLayout(
                nachricht,
                senden
        );

        eingabe.setWidthFull();
        eingabe.setFlexGrow(1, nachricht);

        VerticalLayout chat = new VerticalLayout(
                kopf,
                aktionen,
                leererChat,
                eingabe
        );

        chat.setPadding(true);
        chat.setSpacing(true);
        chat.setWidthFull();
        chat.setMinHeight("400px");

        chat.getStyle()
                .set("background", flaeche)
                .set("border", "1px solid " + rahmen)
                .set("border-radius", "16px")
                .set("box-sizing", "border-box");

        return chat;
    }

    /*
     * Diese Methode stellt eine Freundschaftsanfrage dar.
     * Sie wird aktuell noch nicht verwendet, kann aber später
     * mit Daten aus einem Service aufgerufen werden.
     */
    private Div erstelleAnfrage(
            String name,
            String information
    ) {
        Div karte = erstelleKarte();

        Span nameSpan = new Span(name);
        nameSpan.getStyle()
                .set("font-weight", "600");

        Span infoSpan = new Span(information);
        infoSpan.getStyle()
                .set("display", "block")
                .set("font-size", "0.8rem")
                .set("color", sekundaer);

        VerticalLayout daten = new VerticalLayout(
                nameSpan,
                infoSpan
        );
        daten.setPadding(false);
        daten.setSpacing(false);

        Button annehmen = new Button("Annehmen");
        annehmen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        annehmen.getStyle().set("background", burgunder);

        Button ablehnen = new Button("X");

        HorizontalLayout aktionen = new HorizontalLayout(
                annehmen,
                ablehnen
        );

        HorizontalLayout inhalt = new HorizontalLayout(
                daten,
                aktionen
        );

        inhalt.setWidthFull();
        inhalt.setJustifyContentMode(JustifyContentMode.BETWEEN);
        inhalt.setAlignItems(Alignment.CENTER);

        karte.add(inhalt);
        return karte;
    }

    /*
     * Diese Methode stellt einen Freund dar.
     * Sie kann später mit echten Benutzerdaten verwendet werden.
     */
    private Div erstelleFreund(
            String kuerzel,
            String name,
            String status,
            boolean ausgewaehlt
    ) {
        Span kuerzelSpan = new Span(kuerzel);
        kuerzelSpan.getStyle()
                .set("background", "#F1E3E0")
                .set("color", burgunder)
                .set("border-radius", "50%")
                .set("padding", "10px")
                .set("font-size", "0.75rem");

        Span nameSpan = new Span(name);
        nameSpan.getStyle()
                .set("font-weight", "600");

        Span statusSpan = new Span(status);
        statusSpan.getStyle()
                .set("display", "block")
                .set("font-size", "0.75rem")
                .set("color", sekundaer);

        VerticalLayout daten = new VerticalLayout(
                nameSpan,
                statusSpan
        );
        daten.setPadding(false);
        daten.setSpacing(false);

        HorizontalLayout zeile = new HorizontalLayout(
                kuerzelSpan,
                daten
        );
        zeile.setAlignItems(Alignment.CENTER);

        Div eintrag = new Div(zeile);
        eintrag.setWidthFull();

        eintrag.getStyle()
                .set("padding", "12px 8px")
                .set("border-bottom", "1px solid " + rahmen)
                .set("cursor", "pointer");

        if (ausgewaehlt) {
            eintrag.getStyle()
                    .set("background", "#F1E3E0");
        }

        return eintrag;
    }

    /*
     * Diese Methode stellt eine einzelne Nachricht dar.
     * Sie kann später für dynamische Chatnachrichten verwendet werden.
     */
    private Span erstelleNachricht(
            String inhalt,
            boolean eigeneNachricht
    ) {
        Span nachricht = new Span(inhalt);

        nachricht.getStyle()
                .set("padding", "12px")
                .set("border-radius", "10px")
                .set("max-width", "70%")
                .set("background",
                        eigeneNachricht ? "#F1E3E0" : "#F5EEE5")
                .set("align-self",
                        eigeneNachricht ? "flex-end" : "flex-start");

        return nachricht;
    }

    private Div erstelleKarte() {
        Div karte = new Div();
        karte.setWidthFull();

        karte.getStyle()
                .set("background", flaeche)
                .set("border", "1px solid " + rahmen)
                .set("border-radius", "16px")
                .set("padding", "16px")
                .set("box-sizing", "border-box");

        return karte;
    }
}
