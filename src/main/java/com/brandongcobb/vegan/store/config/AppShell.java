package com.brandongcobb.vegan.store.config;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;

//@StyleSheet("context://styles/store-stylesø.css")
import com.vaadin.flow.component.page.Push;

@Push
@CssImport(("./styles/store-styles.css")) // Built-in Lumo Dark Mode
@Theme("vegan")
public class AppShell implements AppShellConfigurator {
    // No need to override configurePage if using Lumo.DARK
}
