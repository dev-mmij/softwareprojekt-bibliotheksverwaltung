package com.softwareprojekt.gruppe8.konto.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;
import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.security.AuthenticatedUser;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;

@Route(value = "konto-einstellungen", layout = MainLayout.class)
@PermitAll
public class KontoeinstellungenView extends VerticalLayout {

    private static final String TEXT = "#352c29";
    private static final String SEKUNDAER = "#82766f";
    private static final String BURGUNDER = "#812f3b";
    private static final String RAHMEN = "#eadbd0";
    private static final String KARTE = "#fffdf9";

    private final AuthenticatedUser authenticatedUser;

    public KontoeinstellungenView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);

        getStyle()
                .set("box-sizing", "border-box")
                .set("max-width", "1200px")
                .set("margin", "0 auto")
                .set("background", "#F7F0E7");

        add(
                createHeader(),
                createLoginDetailsCard(),
                createDeleteAccountCard(),
                createPrivacyNote()
        );
    }

    private Component createHeader() {
        H1 title = new H1("Konto & Einstellungen");
        title.getStyle()
                .set("font-family", "Georgia, serif")
                .set("font-size", "28px")
                .set("color", TEXT)
                .set("margin", "0");

        Paragraph subtitle = new Paragraph(
                "Verwalte deine E-Mail-Adresse, dein Passwort und dein Readly-Konto."
        );
        subtitle.getStyle()
                .set("font-size", "14px")
                .set("color", SEKUNDAER)
                .set("margin", "6px 0 0");

        VerticalLayout header = new VerticalLayout(title, subtitle);
        header.setPadding(false);
        header.setSpacing(false);
        return header;
    }

    private Component createLoginDetailsCard() {
        H2 title = createSectionTitle("Anmeldedaten");

        EmailField emailField = new EmailField("E-Mail-Adresse");
        emailField.setWidthFull();
        emailField.setReadOnly(true);
        emailField.setValue(getCurrentEmail());
        emailField.getStyle()
                .set("--vaadin-input-field-border-radius", "6px")
                .set("--vaadin-input-field-border-color", RAHMEN)
                .set("--vaadin-input-field-background", "#f7f0e7")
                .set("--vaadin-input-field-label-color", TEXT);

        Button emailButton = new Button("E-Mail zurücksetzen", event ->
                Notification.show("Die E-Mail-Änderung ist noch nicht eingerichtet.")
        );
        emailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        styleOutlineButton(emailButton);

        HorizontalLayout emailRow = new HorizontalLayout(emailField, emailButton);
        emailRow.setWidthFull();
        emailRow.setAlignItems(FlexComponent.Alignment.END);
        emailRow.setSpacing(true);
        emailRow.setFlexGrow(1, emailField);
        emailRow.getStyle().set("flex-wrap", "wrap");

        Div separator = new Div();
        separator.getStyle()
                .set("height", "1px")
                .set("width", "100%")
                .set("background", RAHMEN)
                .set("margin", "18px 0");

        Span passwordLabel = new Span("Passwort");
        passwordLabel.getStyle()
                .set("font-size", "14px")
                .set("color", TEXT);

        Paragraph passwordHint = new Paragraph(
                "Setze dein Passwort zurück, um ein neues zu vergeben."
        );
        passwordHint.getStyle()
                .set("font-size", "13px")
                .set("color", SEKUNDAER)
                .set("margin", "6px 0 0");

        VerticalLayout passwordText = new VerticalLayout(passwordLabel, passwordHint);
        passwordText.setPadding(false);
        passwordText.setSpacing(false);

        Button passwordButton = new Button("Passwort zurücksetzen", event ->
                Notification.show("Das Zurücksetzen des Passworts ist noch nicht eingerichtet.")
        );
        passwordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        passwordButton.getStyle()
                .set("background", BURGUNDER)
                .set("color", "white")
                .set("border-radius", "6px");

        HorizontalLayout passwordRow = new HorizontalLayout(passwordText, passwordButton);
        passwordRow.setWidthFull();
        passwordRow.setAlignItems(FlexComponent.Alignment.CENTER);
        passwordRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        passwordRow.getStyle().set("flex-wrap", "wrap");

        VerticalLayout card = new VerticalLayout(title, emailRow, separator, passwordRow);
        styleCard(card);
        return card;
    }

    private Component createDeleteAccountCard() {
        H2 title = createSectionTitle("Konto löschen");

        Paragraph description = new Paragraph(
                "Wenn du dein Konto löschst, werden dein Profil und deine Lesedaten "
                        + "dauerhaft entfernt. Diese Aktion kann nicht rückgängig gemacht werden."
        );
        description.getStyle()
                .set("max-width", "620px")
                .set("font-size", "13px")
                .set("line-height", "1.5")
                .set("color", SEKUNDAER)
                .set("margin", "0");

        Button deleteButton = new Button("Konto löschen", event ->
                Notification.show("Die Kontolöschung ist noch nicht eingerichtet.")
        );
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        styleOutlineButton(deleteButton);
        deleteButton.getStyle().set("color", "#b42318");

        HorizontalLayout content = new HorizontalLayout(description, deleteButton);
        content.setWidthFull();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        content.getStyle().set("flex-wrap", "wrap");

        VerticalLayout card = new VerticalLayout(title, content);
        styleCard(card);
        return card;
    }

    private Component createPrivacyNote() {
        Span lockIcon = new Span();
        lockIcon.add(VaadinIcon.LOCK.create());
        lockIcon.getStyle().set("color", SEKUNDAER);

        Span text = new Span("Deine Anmeldedaten sind nur für dich sichtbar.");
        text.getStyle()
                .set("font-size", "12px")
                .set("color", SEKUNDAER);

        HorizontalLayout note = new HorizontalLayout(lockIcon, text);
        note.setAlignItems(FlexComponent.Alignment.CENTER);
        note.setSpacing(true);
        note.setPadding(false);
        return note;
    }

    private H2 createSectionTitle(String text) {
        H2 title = new H2(text);
        title.getStyle()
                .set("font-family", "Georgia, serif")
                .set("font-size", "19px")
                .set("color", TEXT)
                .set("margin", "0");
        return title;
    }

    private void styleCard(VerticalLayout card) {
        card.setWidthFull();
        card.setPadding(true);
        card.setSpacing(true);
        card.getStyle()
                .set("box-sizing", "border-box")
                .set("background", KARTE)
                .set("border", "1px solid " + RAHMEN)
                .set("border-radius", "8px")
                .set("box-shadow", "0 4px 14px rgba(53, 44, 41, 0.05)");
    }

    private void styleOutlineButton(Button button) {
        button.getStyle()
                .set("min-width", "180px")
                .set("border", "1px solid " + BURGUNDER)
                .set("border-radius", "6px")
                .set("background", "transparent")
                .set("color", BURGUNDER);
    }

    private String getCurrentEmail() {
        Optional<User> currentUser = authenticatedUser.get();

        if (currentUser.isEmpty()) {
            return "";
        }

        User user = currentUser.get();
        return user.getEmail() != null ? user.getEmail() : user.getUsername();
    }
}