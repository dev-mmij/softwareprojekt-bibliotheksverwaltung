package com.softwareprojekt.gruppe8.empfehlung.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Empfehlungskarte extends Div {
    public Empfehlungskarte() {
        Image buchCover = new Image("images/empty-plant.png", "Buch-Cover");
        H4 buchTitel = new H4("Buchtitel");
        Paragraph autor = new Paragraph("Autorname");
        Span buchGenre = new Span("Genre");
        Paragraph empfehlungsGrund = new Paragraph("Hier steht der Empfehlungsgrund.");
        Span passungsAbfrage = new Span("Passt das zu dir?");
        Button daumenHoch = new Button("Ja");
        Button daumenRunter = new Button("Nein");
        HorizontalLayout buttonLeiste = new HorizontalLayout(passungsAbfrage, daumenHoch, daumenRunter);
        VerticalLayout buchDetails = new VerticalLayout(buchTitel, autor, buchGenre, empfehlungsGrund, buttonLeiste);
        HorizontalLayout inhalt = new HorizontalLayout(buchCover, buchDetails);
        add(inhalt);
    }
}
