//

//
//
//  Created by Brandon Cobb on 5/20/25.
//

package com.brandongcobb.vegan.store.ui.views;
import com.vaadin.flow.component.Composite;
//  CheckoutView.java
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.brandongcobb.vegan.store.ui.base.*;

@Route("checkout")
@PageTitle("Checkout | The Vyrtuous Project")
public class CheckoutView extends Composite<VerticalLayout> {
    public CheckoutView() {
        getContent().add(new H2("Checkout (template)"));
    }
}
