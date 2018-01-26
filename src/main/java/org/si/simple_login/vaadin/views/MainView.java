package org.si.simple_login.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.stereotype.Component;

import static org.si.simple_login.vaadin.PagePaths.LOGIN;
import static org.si.simple_login.vaadin.ViewNavigator.navigator;

@Component
public class MainView extends CustomComponent implements View {

    public MainView(){

        Label label = new Label("You are logged in.");
        // Upon sign-out, redirect to sign-in
        Button signOut = new Button("Sign out", e -> navigator.navigateTo(LOGIN.getPagePath()));

        setCompositionRoot(new VerticalLayout(label, signOut));
    }
}
