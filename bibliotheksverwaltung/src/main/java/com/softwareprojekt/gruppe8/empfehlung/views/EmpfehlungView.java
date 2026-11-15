package com.softwareprojekt.gruppe8.empfehlung.views;

import com.softwareprojekt.gruppe8.empfehlung.service.EmpfehlungService;
import com.softwareprojekt.gruppe8.empfehlung.service.Empfehlungsdaten;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
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
@RolesAllowed("USER")
public class EmpfehlungView extends VerticalLayout {

    private final EmpfehlungService empfehlungService;

    public EmpfehlungView(EmpfehlungService empfehlungService) {
        this.empfehlungService = empfehlungService;
        //Ueberschrift
        H2 ueberschrift = new H2("Für dich");
        Paragraph seitenBeschreibung = new Paragraph("Entdecke Bücher, die dir gefallen könnten!");

        //Hinzufuegen aller Elemente
        add(ueberschrift, seitenBeschreibung);

        //Karten
        List<Empfehlungsdaten> empfehlungen = empfehlungService.ladeNeueEmpfehlungen();
        empfehlungen.forEach(empfehlungsdatensatz -> add(new Empfehlungskarte(empfehlungsdatensatz)));

    }
}
