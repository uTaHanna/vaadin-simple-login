package org.si.simple_login.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import static org.si.simple_login.vaadin.PagePaths.LOGIN;
import static org.si.simple_login.vaadin.PagePaths.MAIN;
import static org.si.simple_login.vaadin.PagePaths.SIGNUP;

@SpringUI
public class ViewNavigator extends UI {

    public static SpringNavigator navigator;
    private final View logInView;
    private final View signUpView;
    private final View mainView;

    @Autowired
    public ViewNavigator(SpringNavigator navigator, View logInView,
                         View signUpView, View mainView){

        ViewNavigator.navigator = navigator;
        this.logInView = logInView;
        this.signUpView = signUpView;
        this.mainView = mainView;
    }

    public void init(VaadinRequest request){

        navigator.init(this, this);
        navigator.addView(LOGIN.getPagePath(), logInView);
        navigator.addView(SIGNUP.getPagePath(), signUpView);
        navigator.addView(MAIN.getPagePath(), mainView);
    }
}
