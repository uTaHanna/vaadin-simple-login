package org.si.simple_login.vaadin;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import org.si.simple_login.vaadin.views.LogInView;
import org.si.simple_login.vaadin.views.MainView;
import org.si.simple_login.vaadin.views.SignUpView;
import org.springframework.beans.factory.annotation.Autowired;

import static org.si.simple_login.vaadin.PagePaths.LOGIN;
import static org.si.simple_login.vaadin.PagePaths.MAIN;
import static org.si.simple_login.vaadin.PagePaths.SIGNUP;

@SpringUI
public class ViewNavigator extends UI {

    public static SpringNavigator navigator;
    private final LogInView logInView;
    private final SignUpView signUpView;
    private final MainView mainView;

    @Autowired
    public ViewNavigator(LogInView logInView, SignUpView signUpView,
                         MainView mainView, SpringNavigator navigator){

        this.logInView = logInView;
        this.signUpView = signUpView;
        this.mainView = mainView;
        ViewNavigator.navigator = navigator;
    }

    public void init(VaadinRequest request){

        navigator.init(this, this);
        navigator.addView(LOGIN.getPagePath(), logInView);
        navigator.addView(SIGNUP.getPagePath(), signUpView);
        navigator.addView(MAIN.getPagePath(), mainView);
    }
}
