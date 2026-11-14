package com.softwareprojekt.gruppe8;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet("styles/styles.css")
public class Application implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings){
        settings.addFavIcon("icon", "icons/logo.png", "32x32");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
