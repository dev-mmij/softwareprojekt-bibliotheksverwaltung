package com.softwareprojekt.gruppe8.common.views;

import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.security.AuthenticatedUser;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Layout
@AnonymousAllowed
@StyleSheet("styles/styles.css")
public class MainLayout extends AppLayout {

    private final AuthenticatedUser authenticatedUser;

    public MainLayout(
            AuthenticatedUser authenticatedUser,
            com.vaadin.flow.server.auth.AccessAnnotationChecker accessChecker
    ) {
        this.authenticatedUser = authenticatedUser;

        setPrimarySection(Section.NAVBAR);
        addDrawerContent();
        addHeaderContent();
        addClassName("readly-layout");
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        addToNavbar(true, toggle, createLogo(), createUserMenu());
    }

    private void addDrawerContent() {
        addToDrawer(new Scroller(createNavigation()));
    }

    private Div createNavigation() {
        Div nav = new Div();
        nav.addClassName("navigation");

        SideNav entdeckenNav = createSideNavGroup("Entdecken");
        SideNav lesenNav = createSideNavGroup("Lesen");
        SideNav kontoNav = createSideNavGroup("Mein Konto");
        SideNav handelNav = createSideNavGroup("Handel");
        SideNav adminNav = createSideNavGroup("Administration");
        adminNav.addClassName("admin-navigation");

        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();

        menuEntries.forEach(entry -> {
            double placement = entry.order() != null ? entry.order() : 0.0;

            SideNavItem item;
            if (entry.icon() != null) {
                item = new SideNavItem(
                        entry.title(),
                        entry.path(),
                        new SvgIcon(entry.icon())
                );
            } else {
                item = new SideNavItem(entry.title(), entry.path());
            }

            if (placement >= 900) {
                adminNav.addItem(item);
            } else if (placement >= 500) {
                kontoNav.addItem(item);
            } else if (placement >= 300) {
                handelNav.addItem(item);
            } else if (placement >= 200) {
                lesenNav.addItem(item);
            } else {
                entdeckenNav.addItem(item);
            }
        });

        nav.add(entdeckenNav, lesenNav, handelNav, kontoNav);

        if (!adminNav.getItems().isEmpty()) {
            nav.add(adminNav);
        }

        return nav;
    }

    private Div createUserMenu() {
        Div layout = new Div();
        layout.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "0.5rem");

        Optional<User> maybeUser = authenticatedUser.get();

        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getUsername());

            byte[] profilbild = user.getProfil() != null
                    ? user.getProfil().getProfilbild()
                    : null;

            if (profilbild != null && profilbild.length > 0) {
                DownloadHandler imageHandler = DownloadHandler.fromInputStream(
                        event -> new DownloadResponse(
                                new ByteArrayInputStream(profilbild),
                                "profile-pic",
                                "image/jpeg",
                                profilbild.length
                        )
                );

                avatar.setImageHandler(imageHandler);
            }

            avatar.getElement().setAttribute("tabindex", "-1");

            Button settingsButton = new Button(VaadinIcon.COG.create());
            settingsButton.setAriaLabel("Kontoeinstellungen");
            settingsButton.setTooltipText("Kontoeinstellungen");
            settingsButton.getStyle()
                    .set("background", "transparent")
                    .set("color", "#82766f")
                    .set("padding", "0.5rem")
                    .set("min-width", "2.5rem");

            settingsButton.addClickListener(event ->
                    getUI().ifPresent(ui -> ui.navigate("konto-einstellungen"))
            );

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");

            Div userDisplay = new Div();
            userDisplay.add(avatar);
            userDisplay.add(user.getUsername());
            userDisplay.add(new Icon("lumo", "dropdown"));
            userDisplay.addClassName("benutzer-anzeige");

            userName.add(userDisplay);
            userName.getSubMenu().addItem(
                    "Abmelden",
                    event -> authenticatedUser.logout()
            );

            layout.add(settingsButton, userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Anmelden");
            loginLink.setText("Anmelden");

            Anchor registerLink = new Anchor("registrieren", "Registrieren");
            registerLink.setText("Registrieren");

            layout.add(loginLink, registerLink);
        }

        layout.addClassName("benutzer-menue");
        return layout;
    }

    private Component createLogo() {
        Image logoImage = new Image("icons/logo.png", "Readly Logo");
        logoImage.addClassName("logo-image");

        Span logoText = new Span("Readly");
        logoText.addClassName("logo-text");

        Div logoDiv = new Div(logoImage, logoText);
        logoDiv.addClassName("logo");

        return logoDiv;
    }

    private SideNav createSideNavGroup(String header) {
        SideNav sideNav = new SideNav(header);
        sideNav.addClassName("seiten-navigation");
        return sideNav;
    }
}
