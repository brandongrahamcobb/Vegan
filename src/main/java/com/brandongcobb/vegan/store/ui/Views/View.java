//
//  View.java
//  
//
//  Created by Brandon Cobb on 5/19/25.
//
package com.brandongcobb.vegan.store.ui.views;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public interface View extends BeforeEnterObserver {
   default void buildLayout(){
       // placeholder
   }
   default void beforeEnter(BeforeEnterEvent event){
       // placeholder
   }
}
