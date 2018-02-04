package org.si.simple_login.vaadin.views;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import org.si.simple_login.domain.User;
import org.si.simple_login.repository.UserAuthenticationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.si.simple_login.vaadin.ViewPaths.LOGIN;
import static org.si.simple_login.vaadin.ViewNavigator.navigator;

@Component
public class SignUpView extends CustomComponent implements View {

    private UserAuthenticationDAO userAuthenticationDAOSQL;

    private Binder<User> userBinder = new Binder<>();
    private User user = new User("", "", "");
    private TextField userNameTextField = new TextField("User Name");
    private TextField emailTextField = new TextField("Email");
    private PasswordField passwordTextField = new PasswordField("Password");
    private Button signUpButton = new Button("Sign up", e -> signUp(user));

    @Autowired
    public void setUserAuthenticationDAOSQL(UserAuthenticationDAO userAuthenticationDAOSQL){

        this.userAuthenticationDAOSQL = userAuthenticationDAOSQL;
    }

    public SignUpView(){

        // Initialize and arrange layout components
        FormLayout signUpFormLayout = new FormLayout(userNameTextField, emailTextField,
                                                     passwordTextField, signUpButton);
        VerticalLayout signUpPageLayout = new VerticalLayout(signUpFormLayout);
        signUpFormLayout.setSizeUndefined();
        signUpPageLayout.setSizeFull();
        signUpPageLayout.setComponentAlignment(signUpFormLayout, Alignment.TOP_CENTER);

        // Bind the user object to text fields for reading in form inputs
        userBinder.bind(userNameTextField, User::getUserName, User::setUserName);
        userBinder.bind(emailTextField, User::getEmail, User::setEmail);
        userBinder.bind(passwordTextField, User::getPassword, User::setPassword);
        userBinder.setBean(user);

        // Set up button and fields
        signUpButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        userNameTextField.setRequiredIndicatorVisible(true);
        emailTextField.setRequiredIndicatorVisible(true);
        passwordTextField.setRequiredIndicatorVisible(true);

        setCompositionRoot(signUpPageLayout);
    }

    /**
     * Register a new user
     * @param userRequest User object encapsulating the input form data, namely user_name, email, and password
     */
    private void signUp(User userRequest){

        try {
            userAuthenticationDAOSQL.addNewUser(userRequest);
            navigator.navigateTo(LOGIN.getPagePath());
        } catch (Exception e){

            Notification.show("Sign up failed: " + e.getMessage(),
                    Notification.Type.ERROR_MESSAGE);
        }
    }
}
