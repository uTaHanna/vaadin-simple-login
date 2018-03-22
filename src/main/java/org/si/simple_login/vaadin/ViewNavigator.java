package org.si.simple_login.vaadin;

import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.si.simple_login.repository.UserAuthenticationDAO;
import org.springframework.beans.factory.annotation.Autowired;

import static org.si.simple_login.vaadin.ViewPaths.LOGIN;
import static org.si.simple_login.vaadin.ViewPaths.MAIN;
import static org.si.simple_login.vaadin.ViewPaths.SIGN_UP;

/**
 * The basic UI design of this app and so the related backend functionalities are
 * based on the Vaadin tutorial by Alejandro Duarte
 * (https://vaadin.com/blog/implementing-remember-me-with-vaadin). More generally,
 * my underlying background knowledge of Vaadin apps owes to another tutorial by
 * the same author (https://vaadin.com/blog/building-a-web-ui-for-mysql-databases-in-plain-java-).
 *
 * More detailed orientation on the use of Vaadin binders was gained from the tutorial by Kirill Bulatov
 * (https://vaadin.com/blog/15624687). The page navigation is based on the official
 * Vaadin documentation (https://vaadin.com/docs/v8/framework/advanced/advanced-navigator.html#advanced.navigator
 * https://vaadin.com/docs/v8/framework/application/application-architecture.html), while basic knowledge of
 * use of Spring beans in Vaadin was learnt from this tutorial: https://vaadin.github.io/spring-tutorial/
 *
 * View access control logic is from the example in the official Vaadin documentation:
 * https://vaadin.com/docs/v8/framework/articles/AccessControlForViews.html
 */

@SpringUI(path="simple_login")
@PushStateNavigation
public class ViewNavigator extends UI {

    public static SpringNavigator navigator;
    private final View loginView;
    private final View signUpView;
    private final View mainView;
    private final UserAuthenticationDAO userAuthenticationDAOSQL;

    @Autowired
    public ViewNavigator(SpringNavigator navigator, View loginView,
                         View signUpView, View mainView, UserAuthenticationDAO userAuthenticationDAOSQL){

        ViewNavigator.navigator = navigator;
        this.loginView = loginView;
        this.signUpView = signUpView;
        this.mainView = mainView;
        this.userAuthenticationDAOSQL = userAuthenticationDAOSQL;
    }

    public void init(VaadinRequest request){

        navigator.init(this, this);
        navigator.addView(LOGIN.getViewPath(), loginView);
        navigator.addView(SIGN_UP.getViewPath(), signUpView);
        navigator.addView(MAIN.getViewPath(), mainView);

        // restricts access to the main view, allowing only authenticated users to proceed
        navigator.addViewChangeListener(viewChangeEvent -> {
            // beforeViewChange of interface ViewChangeListener expressed by lambda

            boolean accessPermission = false;

            if (viewChangeEvent.getNewView().equals(mainView) &&
                    userAuthenticationDAOSQL.getAuthenticatedUserName() == null){

                Notification.show("Please log in", Notification.Type.ERROR_MESSAGE);
                navigator.navigateTo(LOGIN.getViewPath());
            } else{

                accessPermission = true;
            }

            return accessPermission;
        });
    }
}
