package com.softwareprojekt.gruppe8.empfehlung.views;

import com.softwareprojekt.gruppe8.empfehlung.service.EmpfehlungService;
import com.softwareprojekt.gruppe8.empfehlung.service.Empfehlungsdaten;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

/**
 * View zum Anzeigen der Empfehlungen (Fuer-Dich)
 *
 * @author Niklas Prell
 */

@PageTitle("Für Dich")
@Route("fuer-dich")
@Menu(order = 120, icon = LineAwesomeIconUrl.STAR)
@PermitAll
@StyleSheet("styles/empfehlung/empfehlung.css")
public class EmpfehlungView extends VerticalLayout {

    private final EmpfehlungService empfehlungService;

    public EmpfehlungView(EmpfehlungService empfehlungService) {
        this.empfehlungService = empfehlungService;
        setSizeFull();
        addClassName("empfehlung-view");

        //Ueberschrift
        H2 ueberschrift = new H2("Für dich");
        ueberschrift.addClassName("seiten-titel");
        Paragraph seitenBeschreibung = new Paragraph("Entdecke Bücher, die dir gefallen könnten!");
        seitenBeschreibung.addClassName("seiten-beschreibung");

        //Karten
        Div kartenbereich = new Div();
        kartenbereich.addClassName("kartenbereich");
        List<Empfehlungsdaten> empfehlungen = empfehlungService.ladeNeueEmpfehlungen();
        empfehlungen.forEach(empfehlungsdatensatz -> kartenbereich.add(new Empfehlungskarte(empfehlungsdatensatz)));

        //Hinzufuegen aller Elemente
        add(ueberschrift, seitenBeschreibung, kartenbereich);
        setFlexGrow(1, kartenbereich);
    }
}
