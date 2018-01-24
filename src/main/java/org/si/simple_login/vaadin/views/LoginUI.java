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

import static com.vaadin.ui.UI.getCurrent;
import static org.si.simple_login.vaadin.PagePaths.MAIN_PAGE;
import static org.si.simple_login.vaadin.ViewNavigator.navigator;

@Component
public class LoginUI extends CustomComponent implements View {

    private UserAuthenticationDAO userAuthenticationDAOSQL;
    private Binder<User> userBinder = new Binder<>();
    private User user = new User("", "", "");
    private TextField userNameTextField = new TextField("User Name");
    private PasswordField passwordTextField = new PasswordField("Password");
    private Button signInButton = new Button("Sign in", e ->  signIn(user));
    private Label newUserLabel = new Label("<span style='cursor: pointer; color:blue'>new user?</span>",
            ContentMode.HTML);
    private HorizontalLayout signUpLayout = new HorizontalLayout(newUserLabel);
    private FormLayout logInFormLayout = new FormLayout(userNameTextField,
                                                        passwordTextField,
                                                        signUpLayout,
                                                        signInButton);
    private VerticalLayout logInPageLayout = new VerticalLayout(logInFormLayout);

    @Autowired
    public void setUserAuthenticationDAOSQL(UserAuthenticationDAO userAuthenticationDAOSQL){

        this.userAuthenticationDAOSQL = userAuthenticationDAOSQL;
    }

    public LoginUI(){

        // Bind the user object to text fields for reading in form inputs
        userBinder.bind(userNameTextField, User::getUserName, User::setUserName);
        userBinder.bind(passwordTextField, User::getPassword, User::setPassword);
        userBinder.setBean(user);

        // Set up button and link
        signInButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        signUpLayout.addLayoutClickListener(e -> navigator.navigateTo(""));

        // Arrange page components
        logInFormLayout.setSizeUndefined();
        logInPageLayout.setSizeFull();
        logInPageLayout.setComponentAlignment(logInFormLayout, Alignment.TOP_CENTER);

        setCompositionRoot(logInPageLayout);
    }

    /**
     * If the user has valid credentials, show the (mock) main page; else, show the error message
     * @param userRequest User object encapsulating the input form data, namely user_name and password
     */
    private void signIn(User userRequest){

        if(userAuthenticationDAOSQL.checkAuthentication(userRequest)){

            navigator.navigateTo(MAIN_PAGE.getPagePath());
        } else{

            Notification.show("Invalid user name or password", Notification.Type.ERROR_MESSAGE);
        }
    }
}
