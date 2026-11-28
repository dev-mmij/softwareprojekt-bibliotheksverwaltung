package com.softwareprojekt.gruppe8.konto.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;
import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.konto.service.KontoeinstellungenService;
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
import com.vaadin.flow.component.textfield.PasswordField;
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
    private final KontoeinstellungenService einstellungenService;

    private final EmailField neueEmailField = new EmailField("Neue E-Mail-Adresse");
    private final PasswordField altesPasswortField =
            new PasswordField("Aktuelles Passwort");
    private final PasswordField neuesPasswortField =
            new PasswordField("Neues Passwort");
    private final PasswordField wiederholtesPasswortField =
            new PasswordField("Neues Passwort wiederholen");

    private final EmailField aktuelleEmailField = new EmailField("E-Mail-Adresse");

    public KontoeinstellungenView(
            AuthenticatedUser authenticatedUser,
            KontoeinstellungenService einstellungenService
    ) {
        this.authenticatedUser = authenticatedUser;
        this.einstellungenService = einstellungenService;

        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);

        getStyle()
                .set("box-sizing", "border-box")
                .set("max-width", "1200px")
                .set("margin", "0 auto")
                .set("background", "#F7F0E7");

        configureFields();

        add(
                createHeader(),
                createLoginDetailsCard(),
                createPrivacyNote()
        );
    }

    private void configureFields() {
        aktuelleEmailField.setWidthFull();
        aktuelleEmailField.setReadOnly(true);
        aktuelleEmailField.setValue(getCurrentEmail());
        styleField(aktuelleEmailField);

        neueEmailField.setWidthFull();
        neueEmailField.setPlaceholder("Neue E-Mail-Adresse eingeben");
        neueEmailField.getElement().setAttribute("autocomplete", "email");
        styleField(neueEmailField);

        altesPasswortField.setWidthFull();
        altesPasswortField.setRevealButtonVisible(true);
        altesPasswortField.getElement().setAttribute("autocomplete", "current-password");
        styleField(altesPasswortField);

        neuesPasswortField.setWidthFull();
        neuesPasswortField.setRevealButtonVisible(true);
        neuesPasswortField.getElement().setAttribute("autocomplete", "new-password");
        styleField(neuesPasswortField);

        wiederholtesPasswortField.setWidthFull();
        wiederholtesPasswortField.setRevealButtonVisible(true);
        wiederholtesPasswortField.getElement().setAttribute("autocomplete", "new-password");
        styleField(wiederholtesPasswortField);
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

        Button emailButton = new Button("E-Mail ändern", event -> changeEmail());
        styleOutlineButton(emailButton);

        VerticalLayout emailFields = new VerticalLayout(
                aktuelleEmailField,
                neueEmailField
        );
        emailFields.setPadding(false);
        emailFields.setSpacing(true);
        emailFields.setWidthFull();

        HorizontalLayout emailRow = new HorizontalLayout(emailFields, emailButton);
        emailRow.setWidthFull();
        emailRow.setAlignItems(FlexComponent.Alignment.END);
        emailRow.setSpacing(true);
        emailRow.setFlexGrow(1, emailFields);
        emailRow.getStyle().set("flex-wrap", "wrap");

        Div separator = createSeparator();

        Paragraph passwordHint = new Paragraph(
                "Gib dein aktuelles Passwort ein, um ein neues festzulegen."
        );
        passwordHint.getStyle()
                .set("font-size", "13px")
                .set("color", SEKUNDAER)
                .set("margin", "0 0 12px");

        Button passwordButton = new Button("Passwort ändern", event -> changePassword());
        passwordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        passwordButton.getStyle()
                .set("background", BURGUNDER)
                .set("color", "white")
                .set("border-radius", "6px");

        VerticalLayout passwordFields = new VerticalLayout(
                altesPasswortField,
                neuesPasswortField,
                wiederholtesPasswortField,
                passwordHint,
                passwordButton
        );
        passwordFields.setPadding(false);
        passwordFields.setSpacing(true);
        passwordFields.setWidthFull();

        VerticalLayout card = new VerticalLayout(title, emailRow, separator, passwordFields);
        styleCard(card);
        return card;
    }

    private Component createPrivacyNote() {
        Span lockIcon = new Span();
        lockIcon.add(VaadinIcon.LOCK.create());
        lockIcon.getStyle().set("color", SEKUNDAER);

        Span noteText = new Span("Deine Anmeldedaten sind nur für dich sichtbar.");
        noteText.getStyle()
                .set("font-size", "12px")
                .set("color", SEKUNDAER);

        HorizontalLayout note = new HorizontalLayout(lockIcon, noteText);
        note.setAlignItems(FlexComponent.Alignment.CENTER);
        note.setSpacing(true);
        note.setPadding(false);
        return note;
    }

    private void changeEmail() {
        if (neueEmailField.isEmpty() || !neueEmailField.isInvalid()) {
            // Vaadin EmailField kennzeichnet ungültige Eingaben über isInvalid().
        }

        if (neueEmailField.isEmpty() || neueEmailField.isInvalid()) {
            Notification.show("Bitte gib eine gültige neue E-Mail-Adresse ein.");
            return;
        }

        Optional<User> currentUser = authenticatedUser.get();
        if (currentUser.isEmpty()) {
            Notification.show("Dein Benutzerkonto wurde nicht gefunden.");
            return;
        }

        String currentUsername = currentUser.get().getUsername();

        try {
            einstellungenService.emailAendern(
                    currentUsername,
                    neueEmailField.getValue()
            );

            Notification.show("E-Mail geändert. Bitte melde dich erneut an.");
            authenticatedUser.logout();
            getUI().ifPresent(ui -> ui.navigate("login"));
        } catch (IllegalArgumentException | IllegalStateException e) {
            Notification.show(e.getMessage());
        } catch (RuntimeException e) {
            Notification.show("Die E-Mail-Adresse konnte nicht geändert werden.");
        }
    }

    private void changePassword() {
        if (altesPasswortField.isEmpty()
                || neuesPasswortField.isEmpty()
                || wiederholtesPasswortField.isEmpty()) {
            Notification.show("Bitte fülle alle Passwortfelder aus.");
            return;
        }

        if (!neuesPasswortField.getValue().equals(wiederholtesPasswortField.getValue())) {
            Notification.show("Die neuen Passwörter stimmen nicht überein.");
            return;
        }

        Optional<User> currentUser = authenticatedUser.get();
        if (currentUser.isEmpty()) {
            Notification.show("Dein Benutzerkonto wurde nicht gefunden.");
            return;
        }

        try {
            einstellungenService.passwortAendern(
                    currentUser.get().getUsername(),
                    altesPasswortField.getValue(),
                    neuesPasswortField.getValue()
            );

            Notification.show("Passwort wurde geändert.");
            altesPasswortField.clear();
            neuesPasswortField.clear();
            wiederholtesPasswortField.clear();
        } catch (IllegalArgumentException | IllegalStateException e) {
            Notification.show(e.getMessage());
        } catch (RuntimeException e) {
            Notification.show("Das Passwort konnte nicht geändert werden.");
        }
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

    private Div createSeparator() {
        Div separator = new Div();
        separator.getStyle()
                .set("height", "1px")
                .set("width", "100%")
                .set("background", RAHMEN)
                .set("margin", "10px 0");
        return separator;
    }

    private void styleField(Component field) {
        field.getStyle()
                .set("--vaadin-input-field-border-radius", "6px")
                .set("--vaadin-input-field-border-color", RAHMEN)
                .set("--vaadin-input-field-background", "#fffdf9")
                .set("--vaadin-input-field-label-color", TEXT);
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
        return authenticatedUser.get()
                .map(user -> user.getEmail() != null ? user.getEmail() : user.getUsername())
                .orElse("");
    }
}