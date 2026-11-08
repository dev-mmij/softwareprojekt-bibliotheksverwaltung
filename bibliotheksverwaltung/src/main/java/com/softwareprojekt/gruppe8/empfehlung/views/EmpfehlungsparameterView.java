package com.softwareprojekt.gruppe8.empfehlung.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

/**
 * Admin-Ansicht zum Einstellen der Empfehlungsparameter
 * @author Niklas Prell
 */

@PageTitle("Empfehlungsparameter")
@Route("admin/empfehlungsparamter")
@Menu(order = 100, icon= LineAwesomeIconUrl.COG_SOLID)
@RolesAllowed("ADMIN")

public class EmpfehlungsparameterView extends VerticalLayout {
    public EmpfehlungsparameterView() {
        H2 ueberschrift = new H2("Empfehlungsparameter");
        add(ueberschrift);
    }
}
