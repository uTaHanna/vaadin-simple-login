package org.si.simple_login.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.si.simple_login.repository.UserAuthenticationDAO;
import org.si.simple_login.repository.impl.UserAuthenticationDAOSQL;
import org.springframework.beans.factory.annotation.Autowired;

import static org.si.simple_login.vaadin.ViewNavigator.navigator;

@SpringView(name = MainView.NAME)
@UIScope
public class MainView extends CustomComponent implements View {

    public static final String NAME = "main";
    private UserAuthenticationDAO userAuthenticationDAOSQL;
    private String currentUserName;
    private Label label = new Label();
    private Button signOut = new Button("Sign out", e -> navigator.navigateTo(LoginView.NAME));

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

        currentUserName =
                (String) VaadinSession.getCurrent().getAttribute(UserAuthenticationDAOSQL.AUTHENTICATED_USER_NAME);
        label.setValue("You are logged in, " + currentUserName);
    }

    @Override
    public void beforeLeave (ViewBeforeLeaveEvent event){

        userAuthenticationDAOSQL.signOut();
        event.navigate();
    }
}
