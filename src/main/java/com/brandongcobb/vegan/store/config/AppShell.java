package com.brandongcobb.vegan;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(variant = Lumo.DARK) // Built-in Lumo Dark Mode
public class AppShell implements AppShellConfigurator {
    // No need to override configurePage if using Lumo.DARK
}
