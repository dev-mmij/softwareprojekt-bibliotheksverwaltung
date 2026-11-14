package com.softwareprojekt.gruppe8.views;

import com.softwareprojekt.gruppe8.data.User;
import com.softwareprojekt.gruppe8.security.AuthenticatedUser;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 *
 * @author Niklas Prell
 */
@Layout
@AnonymousAllowed
@StyleSheet("styles/styles.css")
public class MainLayout extends AppLayout {


    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

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

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.forEach(entry -> {
            if (entry.icon() != null) {
                nav.addItem(new SideNavItem(entry.title(), entry.path(), new SvgIcon(entry.icon())));
            } else {
                nav.addItem(new SideNavItem(entry.title(), entry.path()));
            }
        });
        return nav;
    }

    private Div createUserMenu() {
        Div layout = new Div();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            DownloadHandler imageHandler = DownloadHandler
                    .fromInputStream(event -> new DownloadResponse(new ByteArrayInputStream(user.getProfilePicture()),
                            "profile-pic", "image/jpeg", -1));
            avatar.setImageHandler(imageHandler);
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.addClassName("benutzer-anzeige");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
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
        logoDiv.addClassNames("logo");
        return logoDiv;
    }
}
