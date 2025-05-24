package com.brandongcobb.vegan.store.ui.views;
import com.vaadin.flow.component.Composite;
import com.brandongcobb.vegan.store.ui.views.*;
import com.brandongcobb.vegan.store.api.dto.VeganRegistrationRequest;
import com.brandongcobb.vegan.store.api.dto.VeganResponse;
import com.brandongcobb.vegan.store.service.VeganService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.Key;

@Route("register")
@PageTitle("Register | Vegan Store")
@AnonymousAllowed
public class RegistrationView extends Composite<VerticalLayout>{ //} extends View {
    
    // simple bean to back the form
    public static class RegistrationForm {
        private String firstName, lastName, email, password, confirmPassword;
        // getters + setters omitted for brevity
        public String getFirstName() { return firstName; }
        public void   setFirstName(String fn) { firstName = fn; }
        public String getLastName()  { return lastName; }
        public void   setLastName(String ln)  { lastName = ln; }
        public String getEmail()     { return email; }
        public void   setEmail(String e)      { email = e; }
        public String getPassword()  { return password; }
        public void   setPassword(String p)   { password = p; }
        public String getConfirmPassword()   { return confirmPassword; }
        public void   setConfirmPassword(String cp) { confirmPassword = cp; }
    }
    
    private final VeganService veganService;
    
    private final Binder<RegistrationForm> binder = new Binder<>(RegistrationForm.class);
    private final RegistrationForm formBean = new RegistrationForm();
    
    private final TextField      firstName = new TextField("First Name");
    private final TextField      lastName  = new TextField("Last Name");
    private final EmailField     email     = new EmailField("Email");
    private final PasswordField  password  = new PasswordField("Password");
    private final PasswordField  confirm   = new PasswordField("Confirm Password");
    private final Button         register  = new Button("Sign Up");
    private final Anchor         loginLink = new Anchor("login", "Already have an account? Log in");
    
    public RegistrationView(VeganService veganService) {
        this.veganService = veganService;
        
        // Make this layout fill the window and center its children
        getContent().setSizeFull();
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        
        // Build the actual Vaadin FormLayout
        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, email, password, confirm);
        formLayout.setResponsiveSteps(
                                      new FormLayout.ResponsiveStep("0", 1),
                                      new FormLayout.ResponsiveStep("600px", 2)
                                      );
        
        // Binder rules
        binder.forField(firstName)
        .asRequired("First name is required")
        .withValidator(new StringLengthValidator("1–50 characters", 1, 50))
        .bind(RegistrationForm::getFirstName, RegistrationForm::setFirstName);
        
        binder.forField(lastName)
        .asRequired("Last name is required")
        .withValidator(new StringLengthValidator("1–50 characters", 1, 50))
        .bind(RegistrationForm::getLastName, RegistrationForm::setLastName);
        
        binder.forField(email)
        .asRequired("Email is required")
        .withValidator(new EmailValidator("Must be a valid email"))
        .bind(RegistrationForm::getEmail, RegistrationForm::setEmail);
        
        binder.forField(password)
        .asRequired("Password is required")
        .withValidator(new StringLengthValidator("At least 8 characters", 8, null))
        .bind(RegistrationForm::getPassword, RegistrationForm::setPassword);
        
        binder.forField(confirm)
        .asRequired("Please confirm your password")
        .withValidator(cp -> cp.equals(password.getValue()), "Passwords do not match")
        .bind(RegistrationForm::getConfirmPassword, RegistrationForm::setConfirmPassword);
        
        register.addClickListener(e -> signUp());
        register.addClickShortcut(Key.ENTER);
        
        // Wrap form + buttons in a 'card' container
        Div card = new Div();
        card.getStyle().set("max-width", "400px")      // constrain width
        .set("width", "100%")          // but fill available up to that
        .set("padding", "1em")         // some breathing room
        .set("box-shadow", "0 0 10px rgba(0,0,0,0.1)")
        .set("border-radius", "8px")
        .set("background", "var(--lumo-base-color)");
        card.add(new H2("Create a new account"), formLayout, register, loginLink);
        
        getContent().add(card);
    }
    
    private void signUp() {
        if (!binder.validate().isOk()) {
            Notification.show("Please fix the errors and try again", 3000, Notification.Position.MIDDLE);
            return;
        }
        try {
            binder.writeBean(formBean);
            VeganRegistrationRequest req = new VeganRegistrationRequest(
                                                                              formBean.getFirstName(),
                                                                              formBean.getLastName(),
                                                                              formBean.getEmail(),
                                                                              formBean.getPassword());
            VeganResponse resp = veganService.register(req);
            Notification.show("Welcome, " + resp.firstName() + "! Please log in.", 3000, Notification.Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.navigate("login"));
        } catch (ValidationException ex) {
            // shouldn't happen, we've already validated
        } catch (ResponseStatusException ex) {
            Notification.show("Registration failed: " + ex.getReason(), 4000, Notification.Position.MIDDLE);
        }
    }
}
