package com.softwareprojekt.gruppe8.empfehlung.views;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

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

    public EmpfehlungView(){
        //Ueberschrift
        H2 ueberschrift = new H2("Für dich");
        Paragraph seitenBeschreibung = new Paragraph("Entdecke Bücher, die dir gefallen könnten!");

        //Karte
        Empfehlungskarte karte = new Empfehlungskarte();

        //Hinzufuegen aller Elemente
        add(ueberschrift, seitenBeschreibung, karte);
    }
}
