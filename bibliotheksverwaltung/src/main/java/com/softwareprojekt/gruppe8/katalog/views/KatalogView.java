package com.softwareprojekt.gruppe8.katalog.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;
import com.softwareprojekt.gruppe8.katalog.entity.Autor;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.katalog.entity.Genre;
import com.softwareprojekt.gruppe8.katalog.service.BuchService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.stream.Collectors;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Buchkatalog")
@Route(value = "katalog", layout = MainLayout.class) // Falls ihr ein MainLayout nutzt, hier layout = MainLayout.class ergänzen
@Menu(title = "Büchersuche", order = 4, icon = LineAwesomeIconUrl.SEARCH_SOLID) // Falls ihr ein Menü nutzt, hier die Menüeinträge ergänzen
@PermitAll
public class KatalogView extends VerticalLayout {

    private final BuchService buchService;
    private final Grid<Buch> grid = new Grid<>(Buch.class, false);
    private final TextField filterText = new TextField();

    public KatalogView(BuchService buchService) {
        this.buchService = buchService;

        setSizeFull();
        configureFilter();
        configureGrid();

        add(
            new H2("Büchersuche"),
            new HorizontalLayout(filterText),
            grid
        );

        updateList();
    }

    private void configureFilter() {
        filterText.setPlaceholder("Nach Buchtitel suchen...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(Buch::getTitel).setHeader("Titel").setSortable(true);
        grid.addColumn(Buch::getIsbn).setHeader("ISBN");
        grid.addColumn(Buch::getSeitenzahl).setHeader("Seiten").setSortable(true);

        grid.addColumn(buch -> buch.getVerlag() != null ? buch.getVerlag().getName() : "-")
            .setHeader("Verlag").setSortable(true);

        grid.addColumn(buch -> buch.getAutoren().stream()
                .map(Autor::getName)
                .collect(Collectors.joining(", ")))
            .setHeader("Autoren");

        grid.addColumn(buch -> buch.getGenres().stream()
                .map(Genre::getBezeichnung)
                .collect(Collectors.joining(", ")))
            .setHeader("Genres");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList() {
        grid.setItems(buchService.searchByTitel(filterText.getValue()));
    }
}
