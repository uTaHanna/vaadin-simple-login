package org.si.simple_login.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.si.simple_login.repository.UserAuthenticationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.si.simple_login.vaadin.ViewPaths.LOGIN;
import static org.si.simple_login.vaadin.ViewNavigator.navigator;

@Component
public class MainView extends CustomComponent implements View {

    private UserAuthenticationDAO userAuthenticationDAOSQL;
    private String currentUserName;
    private Label label = new Label();
    private Button signOut = new Button("Sign out", e -> navigator.navigateTo(LOGIN.getViewPath()));

    @Autowired
    public void setUserAuthenticationDAOSQL(UserAuthenticationDAO userAuthenticationDAOSQL){

        this.userAuthenticationDAOSQL = userAuthenticationDAOSQL;
    }

    public MainView(){

        // Initialize layout component
        VerticalLayout mainViewLayout = new VerticalLayout(label, signOut);

        setCompositionRoot(mainViewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event){

        currentUserName = userAuthenticationDAOSQL.getAuthenticatedUserName();
        label.setValue("You are logged in, " + currentUserName);
    }

    @Override
    public void beforeLeave (ViewBeforeLeaveEvent event){

        userAuthenticationDAOSQL.signOut();
        event.navigate();
    }
}
