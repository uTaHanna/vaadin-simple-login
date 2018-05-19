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
import com.vaadin.ui.MenuBar;
import org.si.simple_login.repository.UserAuthenticationDAO;
import org.si.simple_login.repository.impl.UserAuthenticationDAOSQL;
import org.si.simple_login.vaadin.views.LoginView;
import org.si.simple_login.vaadin.views.MainView;
import org.si.simple_login.vaadin.views.ProfileView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/**
 * The basic UI design of this app and so the related backend functionalities is
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
 *
 * For the navigation bar, the ideas owe to the following sources:
 * https://www.youtube.com/watch?v=-xejxaIQTO8;
 * https://vaadin.com/forum/thread/590939;
 * https://vaadin.com/docs/v8/framework/components/components-menubar.html
 */

@SpringUI(path = "/simple_login")
//@SpringViewDisplay
@PushStateNavigation
@Theme("custom_theme")
public class ViewNavigator extends UI {

    public static SpringNavigator navigator;
    private UserAuthenticationDAO userAuthenticationDAO;

    private ComponentContainer componentContainer = new Navigator.EmptyView();
    private MenuBar navBarLeft = new MenuBar();
    private MenuBar navBarRight = new MenuBar();
    private HashSet<String> restrictedViewNames = new HashSet<>();
    private HashMap<String, Integer> menuMap = new HashMap<>();

    @Autowired
    public ViewNavigator(SpringNavigator navigator, UserAuthenticationDAO userAuthenticationDAO){

        ViewNavigator.navigator = navigator;
        this.userAuthenticationDAO = userAuthenticationDAO;
    }

    public void init(VaadinRequest request){

        // set up the navigation bar; styling is done in the scss file
        navBarLeft.addItem("Home", e -> navigator.navigateTo(MainView.NAME));
        navBarLeft.addItem("Profile", e -> navigator.navigateTo(ProfileView.NAME));

        navBarRight.addItem("Sign Out", e -> signOut());
        navBarRight.addStyleName("navBarRight");

        HorizontalLayout navBarLayout = new HorizontalLayout(navBarLeft, navBarRight);
        navBarLayout.setStyleName("navBarLayout");

        navBarLayout.setVisible(false);

        // associate the navigation items above with their respective indices in the menu bar
        menuMap.put(MainView.NAME, 0);
        menuMap.put(ProfileView.NAME, 1);
        menuMap.put(LoginView.NAME, 2);

        // set up the meta-layout for showing views
        componentContainer.setSizeFull();
        VerticalLayout overarchingLayout = new VerticalLayout(navBarLayout, componentContainer);
        overarchingLayout.setSizeFull();
        overarchingLayout.setStyleName("overarchingLayout");
        setContent(overarchingLayout);

        // initialize navigation with access control; show the nav bar only for authenticated access
        navigator.init(this, componentContainer);
        restrictedViewNames.add(MainView.NAME);
        restrictedViewNames.add(ProfileView.NAME);
        navigator.addViewChangeListener(new ViewChangeListener(){

            String previousViewName;
            List<MenuBar.MenuItem> menuItems = navBarLeft.getItems();

            // allow only authenticated users into the restricted pages; attempts to access by the url will fail
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent){

                boolean accessPermission = false;
                String viewName = extractViewName(viewChangeEvent);

                if(restrictedViewNames.contains(viewName) &&
                        VaadinSession.getCurrent().getAttribute(UserAuthenticationDAOSQL.AUTHENTICATED_USER_NAME) == null){

                    Notification.show("Please log in", Notification.Type.ERROR_MESSAGE);
                    navigator.navigateTo(LoginView.NAME);
                } else{

                    accessPermission = true;
                }
                return accessPermission;
            }

            /**
             * The logic for marking the current active menu item for styling is from
             * https://vaadin.com/docs/v8/framework/components/components-menubar.html
             *
             * It seems, however, more suitable to select the active menu item here, rather than
             * in the menuSelected of the MenuBar.Command, if one wants to set the initial
             * view for special styling as well before the user clicks any menu item. Also, these
             * few lines nicely can handle the cases of browser refresh.
             */
            @Override
            public void afterViewChange(ViewChangeEvent viewChangeEvent){

                String currentViewName = extractViewName(viewChangeEvent);

                if(restrictedViewNames.contains(currentViewName)){

                    navBarLayout.setVisible(true);
                    MenuBar.MenuItem currentView = menuItems.get(menuMap.get(currentViewName));

                    if(previousViewName != null){

                        MenuBar.MenuItem previousView = menuItems.get(menuMap.get(previousViewName));
                        previousView.setStyleName(null);
                    }
                    currentView.setStyleName("activeMenuItem");
                    previousViewName = currentViewName;

                } else{

                    navBarLayout.setVisible(false);
                }
            }
        });
    }

    /**
     * Expresses the current view name in lower case without the suffix
     * @param viewChangeEvent prompts the editing
     * @return processed view name
     */
    private String extractViewName(ViewChangeListener.ViewChangeEvent viewChangeEvent){

        String viewNameSuffix = "view";
        String viewClassNameTemp = viewChangeEvent.getNewView().getClass().getSimpleName().toLowerCase();

        // remove the suffix in a view class name, "view", to facilitate matching
        return viewClassNameTemp.split(viewNameSuffix)[0];
    }

    /**
     * Do the logout procedures, including redirection to the login page
     */
    private void signOut(){

        navigator.navigateTo(LoginView.NAME);
        userAuthenticationDAO.signOut();
    }
}
