package com.softwareprojekt.gruppe8.empfehlung.views;

import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlungsparameter;
import com.softwareprojekt.gruppe8.empfehlung.service.EmpfehlungsparameterService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.slider.DecimalSlider;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import static com.vaadin.copilot.shaded.io.netty.handler.codec.http.HttpHeaders.setHeader;

/**
 * Admin-Ansicht zum Einstellen der Empfehlungsparameter
 * @author Niklas Prell
 */

@PageTitle("Empfehlungsparameter")
@Route("admin/empfehlungsparamter")
@Menu(order = 100, icon= LineAwesomeIconUrl.COG_SOLID)
@RolesAllowed("ADMIN")

public class EmpfehlungsparameterView extends VerticalLayout {

    private final EmpfehlungsparameterService  empfehlungsparameterService;

    public EmpfehlungsparameterView(EmpfehlungsparameterService empfehlungsparameterService) {

        this.empfehlungsparameterService = empfehlungsparameterService;

        //Ueberschrift
        H2 ueberschrift = new H2("Empfehlungsparameter");
        Paragraph seitenBeschreibung = new Paragraph("Lege fest, wie Readly Interessen gewichtet und neue Bücher empfiehlt.");

        //Tabelle der Empfehlungsparameter
        Grid<Empfehlungsparameter> empfehlungsparameterGrid = new Grid<>();
        empfehlungsparameterGrid.addComponentColumn(this::parameterMitBeschreibung).setHeader("Parameter & Beschreibung");
        empfehlungsparameterGrid.addComponentColumn(this::werteSlider).setHeader("Wert");
        empfehlungsparameterGrid.setItems(empfehlungsparameterService.getEmpfehlungsparameters());
        empfehlungsparameterGrid.setAllRowsVisible(true);



        //Hinzufuegen aller Elemente
        add(ueberschrift,  seitenBeschreibung, empfehlungsparameterGrid);
    }

    /**
     * Liefert einen VerticalLayout mit der Kennung eines Empfehlungsparameters mit der Beschreibung unten
     * drunter.
     * @param empfehlungsparameter
     * @return VerticalLayout mit Kennung und Beschreibung eiens Empfehlungsparameters
     */
    private VerticalLayout parameterMitBeschreibung(Empfehlungsparameter empfehlungsparameter) {
        H4 parameterKennung = new H4(empfehlungsparameter.getKennung());
        Paragraph parameterBeschreibung = new Paragraph(empfehlungsparameter.getBeschreibung());
        VerticalLayout zelle = new VerticalLayout(parameterKennung, parameterBeschreibung);
        zelle.setSpacing(false);
        return zelle;
    }

    /**
     * Liefert ein HorizontalLayout mit den Werten eines Parameters als Slider und Feld.
     * @param empfehlungsparameter
     * @return HorizontalLayout mit DecimalSlider und NumberField zum Einstellen eines Empfehlungsparamters.
     */
    private HorizontalLayout werteSlider(Empfehlungsparameter empfehlungsparameter) {
        DecimalSlider slider = new DecimalSlider(0.0, 10.0);
        slider.setValue(empfehlungsparameter.getWert());
        slider.setMinMaxVisible(true);
        NumberField feld = new NumberField();
        feld.setMin(0.0);
        feld.setMax(10.0);
        feld.setStep(1.0);
        feld.setValue(empfehlungsparameter.getWert());
        return new HorizontalLayout(slider, feld);
    }



}
