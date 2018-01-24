package org.si.simple_login.vaadin;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import org.si.simple_login.vaadin.views.LoginUI;
import org.si.simple_login.vaadin.views.MainPageUI;
import org.springframework.beans.factory.annotation.Autowired;

import static org.si.simple_login.vaadin.PagePaths.MAIN_PAGE;

@SpringUI
public class ViewNavigator extends UI {

    public static SpringNavigator navigator;
    private final LoginUI loginUI;
    private final MainPageUI mainPageUI;

    @Autowired
    public ViewNavigator(LoginUI loginUI, MainPageUI mainPageUI, SpringNavigator navigator){

        this.loginUI = loginUI;
        this.mainPageUI = mainPageUI;
        ViewNavigator.navigator = navigator;
    }

    public void init(VaadinRequest request){

        navigator.init(this, this);
        navigator.addView("", loginUI);
        navigator.addView(MAIN_PAGE.getPagePath(), mainPageUI);
    }
}
