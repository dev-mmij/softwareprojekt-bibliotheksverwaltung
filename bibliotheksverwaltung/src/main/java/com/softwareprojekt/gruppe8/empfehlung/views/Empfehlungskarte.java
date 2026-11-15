package com.softwareprojekt.gruppe8.empfehlung.views;

import com.softwareprojekt.gruppe8.empfehlung.service.Empfehlungsdaten;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class Empfehlungskarte extends Div {
    public Empfehlungskarte(Empfehlungsdaten empfehlungsdaten) {
        Image buchCover = new Image("images/empty-plant.png", "Buch-Cover");
        H4 buchTitel = new H4(empfehlungsdaten.getBuchTitel());
        Paragraph autor = new Paragraph(empfehlungsdaten.getAutor());
        HorizontalLayout genreLeiste = new HorizontalLayout();
        for(String buchGenre : empfehlungsdaten.getBuchGenres()){
            genreLeiste.add(new Span(buchGenre));
        }
        Paragraph empfehlungsGrund = new Paragraph(empfehlungsdaten.getEmpfehlungsgrund());
        Span passungsAbfrage = new Span("Passt das zu dir?");
        Button daumenHoch = new Button("Ja");
        Button daumenRunter = new Button("Nein");
        HorizontalLayout buttonLeiste = new HorizontalLayout(passungsAbfrage, daumenHoch, daumenRunter);
        VerticalLayout buchDetails = new VerticalLayout(buchTitel, autor, genreLeiste, empfehlungsGrund, buttonLeiste);
        HorizontalLayout inhalt = new HorizontalLayout(buchCover, buchDetails);
        add(inhalt);
    }
}
