package org.si.simple_login.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.si.simple_login.repository.impl.UserAuthenticationDAOSQL;
import org.si.simple_login.vaadin.views.LoginView;
import org.si.simple_login.vaadin.views.MainView;
import org.springframework.beans.factory.annotation.Autowired;


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
 * https://vaadin.com/docs/v8/framework/application/application-architecture.html,
 * https://vaadin.com/docs/v8/framework/advanced/advanced-spring.html), while basic knowledge of
 * use of Spring beans in Vaadin was learnt from this tutorial: https://vaadin.github.io/spring-tutorial/
 *
 * View access control logic is from the example in the official Vaadin documentation:
 * https://vaadin.com/docs/v8/framework/articles/AccessControlForViews.html
 */

@SpringUI(path = "/simple_login")
//@SpringViewDisplay
@PushStateNavigation
@Theme("custom_theme")
public class ViewNavigator extends UI {

    public static SpringNavigator navigator;

    @Autowired
    public void setNavigator(SpringNavigator navigator){

        ViewNavigator.navigator = navigator;
    }

    public void init(VaadinRequest request){

        navigator.init(this, this);

        // allows only authenticated users into the main page; attempts to access by the url will fail
        navigator.addViewChangeListener(viewChangeEvent -> {
            // beforeViewChange of interface ViewChangeListener implemented by lambda

            boolean accessPermission = false;
            String viewClassName = viewChangeEvent.getNewView().getClass().getSimpleName().toLowerCase();

            if (viewClassName.contains(MainView.NAME) &&
                    VaadinSession.getCurrent().getAttribute(UserAuthenticationDAOSQL.AUTHENTICATED_USER_NAME) == null){

                Notification.show("Please log in", Notification.Type.ERROR_MESSAGE);
                navigator.navigateTo(LoginView.NAME);
            } else{

                accessPermission = true;
            }

            return accessPermission;
        });
    }
}
