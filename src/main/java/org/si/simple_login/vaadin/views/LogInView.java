package org.si.simple_login.vaadin.views;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.si.simple_login.domain.User;
import org.si.simple_login.repository.UserAuthenticationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.si.simple_login.vaadin.PagePaths.MAIN;
import static org.si.simple_login.vaadin.PagePaths.SIGNUP;
import static org.si.simple_login.vaadin.ViewNavigator.navigator;

@Component
public class LogInView extends CustomComponent implements View {

    private UserAuthenticationDAO userAuthenticationDAOSQL;

    private Binder<User> userBinder = new Binder<>();
    private User user = new User("", "");
    private TextField userNameTextField = new TextField("User Name");
    private PasswordField passwordTextField = new PasswordField("Password");
    private Button signInButton = new Button("Sign in", e ->  signIn(user));
    private Label newUserLabel = new Label("<span style='cursor: pointer; color:blue'>new user?</span>",
            ContentMode.HTML);

    @Autowired
    public void setUserAuthenticationDAOSQL(UserAuthenticationDAO userAuthenticationDAOSQL){

        this.userAuthenticationDAOSQL = userAuthenticationDAOSQL;
    }

    public LogInView(){

        // Initialize and arrange layout components
        HorizontalLayout signUpLayout = new HorizontalLayout(newUserLabel);
        FormLayout logInFormLayout = new FormLayout(userNameTextField, passwordTextField,
                signUpLayout, signInButton);
        VerticalLayout logInPageLayout = new VerticalLayout(logInFormLayout);
        logInFormLayout.setSizeUndefined();
        logInPageLayout.setSizeFull();
        logInPageLayout.setComponentAlignment(logInFormLayout, Alignment.TOP_CENTER);

        // Bind the user object to text fields for reading in form inputs
        userBinder.bind(userNameTextField, User::getUserName, User::setUserName);
        userBinder.bind(passwordTextField, User::getPassword, User::setPassword);
        userBinder.setBean(user);

        // Set up button and link
        signInButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        signUpLayout.addLayoutClickListener(e -> navigator.navigateTo(SIGNUP.getPagePath()));

        setCompositionRoot(logInPageLayout);
    }

    /**
     * If the user has valid credentials, show the (mock) main page; else, show the error message
     * @param userRequest User object encapsulating the input form data, namely user_name and password
     */
    private void signIn(User userRequest){

        if(userAuthenticationDAOSQL.checkAuthentication(userRequest)){

            navigator.navigateTo(MAIN.getPagePath());
        } else{

            Notification.show("Invalid user name or password", Notification.Type.ERROR_MESSAGE);
        }
    }
}
