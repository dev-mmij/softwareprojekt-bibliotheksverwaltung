package com.softwareprojekt.gruppe8.konto.views;

import com.softwareprojekt.gruppe8.common.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "registrieren", layout = MainLayout.class)
@AnonymousAllowed
public class RegistrierenView extends VerticalLayout {

    private static final String BURGUNDER = "#812f3b";
    private static final String TEXT = "#352c29";
    private static final String SEKUNDAER = "#82766f";
    private static final String RAHMEN = "#eadbd0";

    private final EmailField emailField = new EmailField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField repeatPasswordField = new PasswordField();

    public RegistrierenView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setPadding(true);
        setSpacing(false);

        getStyle()
                .set("box-sizing", "border-box")
                .set("background-color", "#F7F0E7");

        Div card = new Div();
        card.getStyle()
                .set("box-sizing", "border-box")
                .set("width", "min(100%, 700px)")
                .set("padding", "28px 40px")
                .set("background", "#fffdf9")
                .set("border", "1px solid " + RAHMEN)
                .set("border-radius", "12px")
                .set("box-shadow", "0 8px 24px rgba(53, 44, 41, 0.08)")
                .set("font-family", "Arial, sans-serif")
                .set("color", TEXT);

        H2 title = new H2("Willkommen bei Readly");
        title.getStyle()
                .set("font-family", "Georgia, serif")
                .set("font-size", "26px")
                .set("line-height", "1.2")
                .set("text-align", "left")
                .set("margin", "0 0 6px")
                .set("color", TEXT);

        Paragraph subtitle = new Paragraph(
                "Erstelle dein Konto und entdecke deine nächste Geschichte."
        );
        subtitle.getStyle()
                .set("font-size", "14px")
                .set("text-align", "left")
                .set("color", SEKUNDAER)
                .set("margin", "0 0 20px");

        configureEmailField();
        configurePasswordField(passwordField, "Passwort", "Passwort eingeben");
        configurePasswordField(
                repeatPasswordField,
                "Passwort wiederholen",
                "Passwort wiederholen"
        );

        Button registerButton = new Button("Registrieren", event -> register());
        registerButton.setWidthFull();
        registerButton.getStyle()
                .set("height", "44px")
                .set("margin-top", "10px")
                .set("background-color", BURGUNDER)
                .set("color", "white")
                .set("border-radius", "6px")
                .set("font-size", "14px")
                .set("font-weight", "600");

        Div separator = new Div();
        separator.getStyle()
                .set("border-top", "1px solid " + RAHMEN)
                .set("margin", "20px 0 14px");

        Span loginText = new Span("Du hast schon einen Account");
        loginText.getStyle()
                .set("font-size", "12px")
                .set("color", SEKUNDAER);

        Anchor loginLink = new Anchor("login", "Anmelden");
        loginLink.getStyle()
                .set("border", "1px solid #a04450")
                .set("border-radius", "6px")
                .set("padding", "7px 13px")
                .set("color", BURGUNDER)
                .set("font-size", "12px")
                .set("text-decoration", "none");

        HorizontalLayout loginRow = new HorizontalLayout(loginText, loginLink);
        loginRow.setWidthFull();
        loginRow.setAlignItems(Alignment.CENTER);
        loginRow.setJustifyContentMode(JustifyContentMode.CENTER);
        loginRow.setSpacing(true);

        card.add(
                title,
                subtitle,
                createFieldBlock("E-Mail", emailField),
                createPasswordBlock("Passwort", passwordField),
                createPasswordBlock("Passwort wiederholen", repeatPasswordField),
                registerButton,
                separator,
                loginRow
        );

        add(card);
    }

    private void configureEmailField() {
        emailField.setPlaceholder("E-Mail eingeben");
        emailField.setWidthFull();
        emailField.getElement().setAttribute("autocomplete", "email");
        emailField.getElement().setAttribute("autocapitalize", "off");
        styleField(emailField);
    }

    private void configurePasswordField(
            PasswordField field,
            String label,
            String placeholder
    ) {
        field.setPlaceholder(placeholder);
        field.setWidthFull();
        field.setRevealButtonVisible(false);
        field.getElement().setAttribute("autocomplete", "new-password");
        styleField(field);
    }

    private void styleField(Component field) {
        field.getStyle()
                .set("box-sizing", "border-box")
                .set("width", "100%")
                .set("--vaadin-input-field-border-radius", "6px")
                .set("--vaadin-input-field-border-color", RAHMEN)
                .set("--vaadin-input-field-background", "#fffdf9")
                .set("--vaadin-input-field-value-font-size", "14px")
                .set("--vaadin-input-field-label-font-size", "14px")
                .set("--vaadin-input-field-label-color", TEXT);
    }

    private VerticalLayout createFieldBlock(String labelText, Component field) {
        VerticalLayout block = new VerticalLayout();
        block.setPadding(false);
        block.setSpacing(false);
        block.setWidthFull();
        block.getStyle().set("margin-bottom", "14px");

        Span label = createFieldLabel(labelText);
        block.add(label, field);
        return block;
    }

    private VerticalLayout createPasswordBlock(String labelText, PasswordField field) {
        VerticalLayout block = new VerticalLayout();
        block.setPadding(false);
        block.setSpacing(false);
        block.setWidthFull();
        block.getStyle().set("margin-bottom", "14px");

        Span label = createFieldLabel(labelText);

        Button revealButton = new Button("Passwort anzeigen");
        revealButton.getStyle()
                .set("flex-shrink", "0")
                .set("padding", "0")
                .set("background", "transparent")
                .set("color", BURGUNDER)
                .set("font-size", "12px");

        revealButton.addClickListener(event -> {
            boolean hidden = "password".equals(
                    field.getElement().getProperty("type", "password")
            );

            field.getElement().setProperty("type", hidden ? "text" : "password");
            revealButton.setText(
                    hidden ? "Passwort verbergen" : "Passwort anzeigen"
            );
        });

        HorizontalLayout inputRow = new HorizontalLayout(field, revealButton);
        inputRow.setWidthFull();
        inputRow.setAlignItems(Alignment.CENTER);
        inputRow.setSpacing(true);
        inputRow.setFlexGrow(1, field);
        inputRow.getStyle().set("min-width", "0");

        block.add(label, inputRow);
        return block;
    }

    private Span createFieldLabel(String text) {
        Span label = new Span(text);
        label.getStyle()
                .set("display", "block")
                .set("width", "100%")
                .set("box-sizing", "border-box")
                .set("padding-left", "12px")
                .set("margin-bottom", "4px")
                .set("text-align", "left")
                .set("font-size", "13px")
                .set("color", TEXT);
        return label;
    }

    private void register() {
        if (emailField.isEmpty()
                || passwordField.isEmpty()
                || repeatPasswordField.isEmpty()) {
            Notification.show("Bitte fülle alle Felder aus.");
            return;
        }

        if (!passwordField.getValue().equals(repeatPasswordField.getValue())) {
            Notification.show("Die Passwörter stimmen nicht überein.");
            return;
        }

        // Hier die Registrierung mit dem vorhandenen Service/Backend verbinden.
        Notification.show("Die Registrierung ist noch nicht mit dem Backend verbunden.");
    }
}