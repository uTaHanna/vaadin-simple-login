package org.si.simple_login.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.*;
import org.si.simple_login.repository.UserAuthenticationDAO;
import org.si.simple_login.repository.impl.UserAuthenticationDAOSQL;
import org.si.simple_login.vaadin.views.LoginView;
import org.si.simple_login.vaadin.views.MainView;
import org.si.simple_login.vaadin.views.ProfileView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;


/**
 * The basic UI design of this app and so the related backend functionalities are
 * based on the Vaadin tutorials by Alejandro Duarte
 * (https://vaadin.com/blog/implementing-remember-me-with-vaadin
 * https://www.youtube.com/watch?v=-xejxaIQTO8). More generally,
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
    private UserAuthenticationDAO userAuthenticationDAO;
    private ComponentContainer componentContainer = new Navigator.EmptyView();
    private MenuBar navBar = new MenuBar();
    private HashSet<String> restrictedViewNames = new HashSet<>();

    @Autowired
    public ViewNavigator(SpringNavigator navigator, UserAuthenticationDAOSQL userAuthenticationDAOSQL){

        ViewNavigator.navigator = navigator;
        this.userAuthenticationDAO = userAuthenticationDAOSQL;
    }

    public void init(VaadinRequest request){

        // to be made into a navigation bar in the style sheet
        navBar.addItem("Home", e -> navigator.navigateTo(MainView.NAME));
        navBar.addItem("Profile", e -> navigator.navigateTo(ProfileView.NAME));
        navBar.addItem("Sign Out", e -> signOut());
        HorizontalLayout navBarLayout = new HorizontalLayout(navBar);
        navBarLayout.setStyleName("navBarLayout");
        navBarLayout.setVisible(false);

        componentContainer.setSizeFull();
        VerticalLayout overarchingLayout = new VerticalLayout(navBarLayout, componentContainer);
        overarchingLayout.setStyleName("overarchingLayout");
        setContent(overarchingLayout);

        navigator.init(this, componentContainer);

        restrictedViewNames.add(MainView.NAME);
        restrictedViewNames.add(ProfileView.NAME);

        navigator.addViewChangeListener(new ViewChangeListener(){

            // allows only authenticated users into the main page; attempts to access by the url will fail
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent){

                boolean accessPermission = false;

                if(isRestrictedView(viewChangeEvent) &&
                        VaadinSession.getCurrent().getAttribute(UserAuthenticationDAOSQL.AUTHENTICATED_USER_NAME) == null){

                    Notification.show("Please log in", Notification.Type.ERROR_MESSAGE);
                    navigator.navigateTo(LoginView.NAME);
                } else{

                    accessPermission = true;
                }

                return accessPermission;
            }

            @Override
            public void afterViewChange(ViewChangeEvent viewChangeEvent){

                if(isRestrictedView(viewChangeEvent)){

                    navBarLayout.setVisible(true);
                } else{

                    navBarLayout.setVisible(false);
                }
            }
        });
    }

    private boolean isRestrictedView(ViewChangeListener.ViewChangeEvent viewChangeEvent){

        String viewNameSuffix = "view";
        String viewClassNameTemp = viewChangeEvent.getNewView().getClass().getSimpleName().toLowerCase();
        String viewClassName = viewClassNameTemp.split(viewNameSuffix)[0];

        return restrictedViewNames.contains(viewClassName);
    }

    private void signOut(){

        navigator.navigateTo(LoginView.NAME);
        userAuthenticationDAO.signOut();
    }
}
