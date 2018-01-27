package org.si.simple_login.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import static org.si.simple_login.vaadin.PagePaths.LOGIN;
import static org.si.simple_login.vaadin.PagePaths.MAIN;
import static org.si.simple_login.vaadin.PagePaths.SIGN_UP;

/**
 * The basic UI design of this app and so the related backend functionalities are
 * based on the Vaadin tutorial by Alejandro Duarte
 * (https://vaadin.com/blog/implementing-remember-me-with-vaadin). More generally,
 * my underlying background knowledge of Vaadin apps owes to another tutorial by
 * the same author (https://vaadin.com/blog/building-a-web-ui-for-mysql-databases-in-plain-java-).
 * More detailed orientation on the use of Vaadin binders was gained from the tutorial by Kirill Bulatov
 * (https://vaadin.com/blog/15624687). The page navigation is based on the official
 * Vaadin documentation (https://vaadin.com/docs/v8/framework/advanced/advanced-navigator.html#advanced.navigator
 * https://vaadin.com/docs/v8/framework/application/application-architecture.html).
 */

@SpringUI
public class ViewNavigator extends UI {

    public static SpringNavigator navigator;
    private final View loginView;
    private final View signUpView;
    private final View mainView;

    @Autowired
    public ViewNavigator(SpringNavigator navigator, View loginView,
                         View signUpView, View mainView){

        ViewNavigator.navigator = navigator;
        this.loginView = loginView;
        this.signUpView = signUpView;
        this.mainView = mainView;
    }

    public void init(VaadinRequest request){

        navigator.init(this, this);
        navigator.addView(LOGIN.getPagePath(), loginView);
        navigator.addView(SIGN_UP.getPagePath(), signUpView);
        navigator.addView(MAIN.getPagePath(), mainView);
    }
}
