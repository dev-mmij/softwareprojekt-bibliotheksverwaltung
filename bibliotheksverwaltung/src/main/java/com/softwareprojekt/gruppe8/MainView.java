package com.softwareprojekt.gruppe8;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {
    public MainView() {
        add(new H1("Bibliotheksverwaltung"));
        add(new Button("Test Button", e -> System.out.println("Vaadin funktioniert!")));
    }
}
