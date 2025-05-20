//

//  CustomerView.java
//  
//
//  Created by Brandon Cobb on 5/20/25.
//

package com.brandongcobb.vegan.store.ui.views;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.brandongcobb.vegan.store.ui.base.*;

@Route("account")
@PageTitle("My Account | The Vyrtuous Project")
public class CustomerView extends Composite<VerticalLayout> {
    public CustomerView() {
        getContent().add(new H2("My Account (template)"));
    }
}
