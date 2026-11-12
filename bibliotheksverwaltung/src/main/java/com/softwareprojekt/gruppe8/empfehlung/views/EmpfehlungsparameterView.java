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
    private final Map<Empfehlungsparameter, DecimalSlider> empfehlungsparameterFelder = new HashMap<>();
    private final Div statusBalken;


    private enum Statusart {ERFOLG, FEHLER, HINWEIS}

    public EmpfehlungsparameterView(EmpfehlungsparameterService empfehlungsparameterService) {

        this.empfehlungsparameterService = empfehlungsparameterService;
        addClassName("empfehlungsparameter-view");
        setSizeFull();

        //Ueberschrift
        H2 ueberschrift = new H2("Empfehlungsparameter");
        ueberschrift.addClassName("seiten-titel");
        Paragraph seitenBeschreibung = new Paragraph("Lege fest, wie Readly Interessen gewichtet und neue Bücher empfiehlt.");
        seitenBeschreibung.addClassName("seiten-beschreibung");

        //Statusbalken
        statusBalken = new Div();
        statusBalken.setVisible(false);
        statusBalken.addClassName("statusmeldung");

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
        HorizontalLayout buttonLeiste = new HorizontalLayout(statusBalken, zuruecksetzButton, speicherButton);
        buttonLeiste.addClassName("button-leiste");


        //Hinzufuegen aller Elemente
        add(ueberschrift,  seitenBeschreibung, empfehlungsparameterGrid, buttonLeiste);
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
            aktualisiereAenderungsstatus();
        });
        feld.addValueChangeListener(e -> {
            if(e.getValue() == null) {
                feld.setValue(parameterDefinition.getMinimum());
            } else{
                double korrigierterWert = parameterDefinition.naechsterGueltigerWert(e.getValue());
                feld.setValue(korrigierterWert);
                slider.setValue(korrigierterWert);
            }
            aktualisiereAenderungsstatus();
        });
        empfehlungsparameterFelder.put(empfehlungsparameter, slider);
        HorizontalLayout wertEingabe = new HorizontalLayout(slider,feld);
        wertEingabe.addClassName("wert-eingabe");
        return wertEingabe;
    }

    private void aktualisiereAenderungsstatus(){
        if(gibtUngespeicherteAenderungen()){
            zeigeStatus(Statusart.HINWEIS, "Ungespeicherte Änderungen");
        }
        else{
            statusBalken.setVisible(false);
        }
    }

    private void zeigeStatus(Statusart status, String text){
        VaadinIcon statusIcon = switch(status){
            case HINWEIS -> VaadinIcon.INFO_CIRCLE;
            case ERFOLG -> VaadinIcon.CHECK_CIRCLE;
            case FEHLER -> VaadinIcon.WARNING;
        };
        statusBalken.removeAll();
        statusBalken.add(new Icon(statusIcon), new Span(text));
        statusBalken.setClassName("status-hinweis", status == Statusart.HINWEIS);
        statusBalken.setClassName("status-erfolg", status == Statusart.ERFOLG);
        statusBalken.setClassName("status-fehler", status == Statusart.FEHLER);
        statusBalken.setVisible(true);
    }

    private void speichern(){
        Map<EmpfehlungsparameterDefinition, Double> neueParameterWerte = new HashMap<>();
        empfehlungsparameterFelder.forEach((parameter, slider) -> neueParameterWerte.put(parameter.getDefinition(),slider.getValue()));
        try{
            empfehlungsparameterService.speichern(neueParameterWerte);
            statusBalken.setVisible(true);
            empfehlungsparameterFelder.clear();
            empfehlungsparameterGrid.setItems(empfehlungsparameterService.getEmpfehlungsparameter());
            zeigeStatus(Statusart.ERFOLG, "Die Änderungen wurden erfolgreich gespeichert.");
        }catch(IllegalArgumentException e){
            zeigeStatus(Statusart.FEHLER, e.getMessage());
        }

    }
    private void zuruecksetzen(){
        try{
            empfehlungsparameterService.zuruecksetzen();
            zeigeStatus(Statusart.ERFOLG, "Die Empfehlungsparameter wurden erfolgreich zurückgesetzt.");
        } catch (IllegalArgumentException e){
            zeigeStatus(Statusart.FEHLER, e.getMessage());
        }
        empfehlungsparameterFelder.clear();
        empfehlungsparameterGrid.setItems(empfehlungsparameterService.getEmpfehlungsparameter());
    }

    private boolean gibtUngespeicherteAenderungen(){
        for(Map.Entry<Empfehlungsparameter, DecimalSlider> eintrag : empfehlungsparameterFelder.entrySet()){
            if(eintrag.getKey().getWert() != eintrag.getValue().getValue()){
                return true;
            }
        }
        return false;
    }
}
