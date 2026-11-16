package com.softwareprojekt.gruppe8.empfehlung.views;

import com.softwareprojekt.gruppe8.empfehlung.service.Empfehlungsdaten;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class Empfehlungskarte extends Div {
    public Empfehlungskarte(Empfehlungsdaten empfehlungsdaten) {
        addClassName("empfehlungskarte");
        Div buchCover = new Div();
        buchCover.addClassName("buch-cover");
        H4 buchTitel = new H4(empfehlungsdaten.getBuchTitel());
        buchTitel.addClassName("buch-titel");
        Paragraph autor = new Paragraph(empfehlungsdaten.getAutor());
        autor.addClassName("buch-autor");
        HorizontalLayout genreLeiste = new HorizontalLayout();
        genreLeiste.addClassName("genre-leiste");
        for(String buchGenre : empfehlungsdaten.getBuchGenres()){
            Span genre = new  Span(buchGenre);
            genreLeiste.add(genre);
            genre.addClassName("buch-genre");
        }
        Paragraph empfehlungsGrund = new Paragraph(empfehlungsdaten.getEmpfehlungsgrund());
        empfehlungsGrund.addClassName("grund");
        Span passungsAbfrage = new Span("Passt das zu dir?");
        passungsAbfrage.addClassName("passung-frage");
        Button daumenHoch = new Button("Ja");
        Button daumenRunter = new Button("Nein");
        HorizontalLayout buttonLeiste = new HorizontalLayout(daumenHoch, daumenRunter);
        HorizontalLayout passungLeiste = new HorizontalLayout(passungsAbfrage, buttonLeiste);
        passungLeiste.addClassName("passung-leiste");
        VerticalLayout buchDetails = new VerticalLayout(buchTitel, autor, genreLeiste, empfehlungsGrund, passungLeiste);
        buchDetails.addClassName("buch-details");
        HorizontalLayout inhalt = new HorizontalLayout(buchCover, buchDetails);
        inhalt.addClassName("karten-inhalt");
        add(inhalt);
    }
}
