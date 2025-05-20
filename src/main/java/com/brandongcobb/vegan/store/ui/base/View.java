//
//  View.java
//  
//
//  Created by Brandon Cobb on 5/19/25.
//
package com.brandongcobb.vegan.store.ui.base;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public abstract class View extends VerticalLayout implements BeforeEnterObserver {

    public abstract void buildLayout();

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        removeAll();
        buildLayout();
    }
}
