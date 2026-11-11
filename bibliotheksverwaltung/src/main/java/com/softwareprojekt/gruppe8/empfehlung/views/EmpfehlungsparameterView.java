package com.softwareprojekt.gruppe8.empfehlung.views;

import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlungsparameter;
import com.softwareprojekt.gruppe8.empfehlung.domain.EmpfehlungsparameterDefinition;
import com.softwareprojekt.gruppe8.empfehlung.service.EmpfehlungsparameterService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.slider.DecimalSlider;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.HashMap;
import java.util.Map;


/**
 * Admin-Ansicht zum Einstellen der Empfehlungsparameter
 * @author Niklas Prell
 */

@PageTitle("Empfehlungsparameter")
@Route("admin/empfehlungsparameter")
@Menu(order = 100, icon= LineAwesomeIconUrl.COG_SOLID)
@RolesAllowed("ADMIN")
@StyleSheet("styles/empfehlung/empfehlungsparameter.css")

public class EmpfehlungsparameterView extends VerticalLayout {

    private final EmpfehlungsparameterService  empfehlungsparameterService;
    private final Grid<Empfehlungsparameter> empfehlungsparameterGrid;
    private final Map<EmpfehlungsparameterDefinition, DecimalSlider> empfehlungsparameterFelder = new HashMap<>();
    private final Div erfolgsBalken;

    public EmpfehlungsparameterView(EmpfehlungsparameterService empfehlungsparameterService) {

        this.empfehlungsparameterService = empfehlungsparameterService;
        addClassName("empfehlungsparameter-view");
        setSizeFull();

        //Ueberschrift
        H2 ueberschrift = new H2("Empfehlungsparameter");
        ueberschrift.addClassName("seiten-titel");
        Paragraph seitenBeschreibung = new Paragraph("Lege fest, wie Readly Interessen gewichtet und neue Bücher empfiehlt.");
        seitenBeschreibung.addClassName("seiten-beschreibung");

        //Erfolgsbalken
        Icon erfolgsIcon = new Icon(VaadinIcon.CHECK);
        Span erfolgsText = new Span(" Speichern der Daten erfolgreich!");
        erfolgsBalken = new Div(erfolgsIcon, erfolgsText);
        erfolgsBalken.setVisible(false);
        erfolgsBalken.addClassName("erfolgsmeldung");

        //Tabelle der Empfehlungsparameter
        empfehlungsparameterGrid = new Grid<>();
        empfehlungsparameterGrid.addComponentColumn(this::parameterMitBeschreibung).setHeader("Parameter & Beschreibung").setFlexGrow(1);
        empfehlungsparameterGrid.addComponentColumn(this::wertEingabe).setHeader("Wert").setWidth("420px").setFlexGrow(0);
        empfehlungsparameterGrid.setItems(empfehlungsparameterService.getEmpfehlungsparameter());
        empfehlungsparameterGrid.addClassName("empfehlungsparameter-grid");
        setFlexGrow(1.0, empfehlungsparameterGrid);

        //Button zum Speichern und Zuruecksetzen
        Button zuruecksetzButton = new Button("Zurücksetzen");
        zuruecksetzButton.addClickListener(e -> zuruecksetzen());
        Button speicherButton = new Button("Speichern");
        speicherButton.addClickListener(e -> speichern());
        speicherButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLeiste = new HorizontalLayout(zuruecksetzButton, speicherButton);
        buttonLeiste.addClassName("button-leiste");


        //Hinzufuegen aller Elemente
        add(ueberschrift,  seitenBeschreibung, erfolgsBalken, empfehlungsparameterGrid, buttonLeiste);
    }

    /**
     * Liefert einen VerticalLayout mit der Kennung eines Empfehlungsparameters mit der Beschreibung unten
     * drunter.
     * @param empfehlungsparameter
     * @return VerticalLayout mit Kennung und Beschreibung eiens Empfehlungsparameters
     */
    private VerticalLayout parameterMitBeschreibung(Empfehlungsparameter empfehlungsparameter) {
        H4 parameterKennung = new H4(empfehlungsparameter.getDefinition().name());
        parameterKennung.addClassName("parameter-kennung");
        Paragraph parameterBeschreibung = new Paragraph(empfehlungsparameter.getBeschreibung());
        parameterBeschreibung.addClassName("parameter-beschreibung");
        VerticalLayout zelle = new VerticalLayout(parameterKennung, parameterBeschreibung);
        zelle.setSpacing(false);
        zelle.addClassName("parameter-zelle");
        return zelle;
    }

    /**
     * Liefert ein HorizontalLayout mit den Werten eines Parameters als Slider und Feld.
     * @param empfehlungsparameter
     * @return HorizontalLayout mit DecimalSlider und NumberField zum Einstellen eines Empfehlungsparamters.
     */
    private HorizontalLayout wertEingabe(Empfehlungsparameter empfehlungsparameter) {
        EmpfehlungsparameterDefinition parameterDefinition = empfehlungsparameter.getDefinition();
        //Slider
        DecimalSlider slider = new DecimalSlider(parameterDefinition.getMinimum(), parameterDefinition.getMaximum());
        slider.setStep(parameterDefinition.getSchrittweite());
        slider.setValue(empfehlungsparameter.getWert());
        slider.setMinMaxVisible(true);
        slider.addClassName("wert-slider");

        //Nummernfeld
        NumberField feld = new NumberField();
        feld.setMin(parameterDefinition.getMinimum());
        feld.setMax(parameterDefinition.getMaximum());
        feld.setStep(parameterDefinition.getSchrittweite());
        feld.setValue(empfehlungsparameter.getWert());
        feld.addClassName("parameter-feld");

        //Synchronisieren der beiden Elemente
        slider.addValueChangeListener(e -> {
            feld.setValue(e.getValue());
            erfolgsBalken.setVisible(false);
        });
        feld.addValueChangeListener(e -> {
            if(e.getValue() == null) {
                feld.setValue(parameterDefinition.getMinimum());
            } else{
                double korrigierterWert = parameterDefinition.naechsterGueltigerWert(e.getValue());
                feld.setValue(korrigierterWert);
                slider.setValue(korrigierterWert);
            }
            erfolgsBalken.setVisible(false);
        });
        empfehlungsparameterFelder.put(empfehlungsparameter.getDefinition(), slider);
        HorizontalLayout wertEingabe = new HorizontalLayout(slider,feld);
        wertEingabe.addClassName("wert-eingabe");
        return wertEingabe;
    }

    private void speichern(){
        Map<EmpfehlungsparameterDefinition, Double> neueParameterWerte = new HashMap<>();
        empfehlungsparameterFelder.forEach((definition, slider) -> neueParameterWerte.put(definition,slider.getValue()));
        try{
            empfehlungsparameterService.speichern(neueParameterWerte);
            erfolgsBalken.setVisible(true);
        }catch(IllegalArgumentException e){
            Notification.show(e.getMessage());
        }

    }
    private void zuruecksetzen(){
        try{
            empfehlungsparameterService.zuruecksetzen();
        } catch (IllegalArgumentException e){
            Notification.show(e.getMessage());
        }
        empfehlungsparameterFelder.clear();
        empfehlungsparameterGrid.setItems(empfehlungsparameterService.getEmpfehlungsparameter());
    }
}
