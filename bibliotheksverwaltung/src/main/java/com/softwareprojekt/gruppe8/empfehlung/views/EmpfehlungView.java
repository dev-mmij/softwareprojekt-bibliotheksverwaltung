package com.softwareprojekt.gruppe8.empfehlung.views;

import com.softwareprojekt.gruppe8.empfehlung.service.EmpfehlungService;
import com.softwareprojekt.gruppe8.empfehlung.service.Empfehlungsdaten;
import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.security.AuthenticatedUser;
import com.vaadin.flow.component.ClientCallable;
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
import java.util.Optional;

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
    private final Optional<User> benutzer;
    private Div kartenbereich;

    public EmpfehlungView(EmpfehlungService empfehlungService, AuthenticatedUser authenticatedUser) {
        this.empfehlungService = empfehlungService;
        benutzer = authenticatedUser.get();
        setSizeFull();
        addClassName("empfehlung-view");

        if(benutzer.isPresent()) {
            //Ueberschrift
            H2 ueberschrift = new H2("Für dich");
            ueberschrift.addClassName("seiten-titel");
            Paragraph seitenBeschreibung = new Paragraph("Entdecke Bücher, die dir gefallen könnten!");
            seitenBeschreibung.addClassName("seiten-beschreibung");

            //Karten
            kartenbereich = new Div();
            kartenbereich.addClassName("kartenbereich");

            //Hinzufuegen aller Elemente
            add(ueberschrift, seitenBeschreibung, kartenbereich);
            setFlexGrow(1, kartenbereich);
            ladeNaechsteRunde();
        } else {
            add(new Paragraph("Bitte melde dich an, um Empfehlungen zu sehen."));
        }
    }

    @ClientCallable
    private void ladeNaechsteRunde() {
        List<Empfehlungsdaten> empfehlungen = empfehlungService.ladeNeueEmpfehlungen(benutzer.get());
        if(!empfehlungen.isEmpty()) {
            for(int i = 0; i< empfehlungen.size(); i++){
                Empfehlungsdaten aktuelleEmpfehlung =  empfehlungen.get(i);
                Empfehlungskarte karte = new Empfehlungskarte(aktuelleEmpfehlung, feedback ->  empfehlungService.speichereFeedback(aktuelleEmpfehlung.getEmpfehlungId(), feedback));
                if(i == Math.max(0, empfehlungen.size()-3)) { //Neuladen der naechsten Empfehlungen ab dieser Karte
                    beobachte(karte);
                }
                kartenbereich.add(karte);
            }
        } else {
            kartenbereich.add(new Paragraph("Keine weiteren Empfehlungen verfügbar."));
        }
    }


    private void beobachte(Empfehlungskarte karte){
        String javaScriptMethode = "const view = $0;" //Speichern der als Parameter uebergebenen EmpfehlungView (festgelegeter Name)
                                    + "new IntersectionObserver((eintraege, beobachter) => {"
                                    + "if (eintraege[0].isIntersecting) {"
                                    + "beobachter.disconnect();"    //Deaktivieren des Observers, damit nicht jede Karte eine neue Empfehlungsrunde generiert
                                    + "view.$server.ladeNaechsteRunde();"
                                    + "}"
                                    + "}).observe(this);";
        karte.getElement().executeJs(javaScriptMethode, getElement());
    }
}
