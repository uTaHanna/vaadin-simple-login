package org.si.simple_login.vaadin.views;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.si.simple_login.domain.User;
import org.si.simple_login.repository.UserAuthenticationDAO;
import org.springframework.beans.factory.annotation.Autowired;

import static org.si.simple_login.vaadin.ViewNavigator.navigator;

@SpringView(name = LoginView.NAME)
@UIScope
public class LoginView extends CustomComponent implements View {

    private UserAuthenticationDAO userAuthenticationDAO;

    public static final String NAME = "";
    private Binder<User> userBinder = new Binder<>();
    private User user = new User("", "");
    private TextField userNameTextField = new TextField("User Name");
    private PasswordField passwordTextField = new PasswordField("Password");
    private Button signInButton = new Button("Sign in", e ->  signIn(user));
    private Label newUserLabel = new Label("<span style='cursor: pointer; color:blue'>new user?</span>",
            ContentMode.HTML);

    @Autowired
    public void setUserAuthenticationDAO(UserAuthenticationDAO userAuthenticationDAOSQL){

        userAuthenticationDAO = userAuthenticationDAOSQL;
    }

    public LoginView(){

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
        signUpLayout.addLayoutClickListener(e -> navigator.navigateTo(SignUpView.NAME));

        setCompositionRoot(logInPageLayout);
    }

    /**
     * If the user has valid credentials, show the main page; else, show the error message
     * @param userRequest User object encapsulating the input form data, namely user_name and password
     */
    private void signIn(User userRequest){

        if(userAuthenticationDAO.checkAuthentication(userRequest)){

            navigator.navigateTo(MainView.NAME);
        } else{

            Notification.show("Invalid user name or password", Notification.Type.ERROR_MESSAGE);
        }
    }

    @Override
    public void beforeLeave (ViewBeforeLeaveEvent event){

        // Clear the text fields before redirection
        userNameTextField.setValue("");
        passwordTextField.setValue("");
        event.navigate();
    }
}
