package com.brandongcobb.vegan.store.ui.views;
import com.brandongcobb.vegan.store.ui.views.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
/**
 * Default view that redirects to login or store based on authentication.
 */
@Route("")
@AnonymousAllowed
@Component
public class DefaultView extends Div implements BeforeEnterObserver {
    
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
        if (loggedIn) {
            event.forwardTo(StoreView.class);
        } else {
            event.forwardTo(LoginView.class);
        }
    }
}
