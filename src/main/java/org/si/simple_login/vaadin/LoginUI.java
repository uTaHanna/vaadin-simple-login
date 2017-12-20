package org.si.simple_login.vaadin;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.si.simple_login.domain.User;
import org.si.simple_login.repository.UserAuthenticationDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The basic design of this UI and so the related backend functionalities are
 * based on the Vaadin tutorial by Alejandro Duarte (https://vaadin.com/blog/implementing-remember-me-with-vaadin)
 * (More generally, my underlying background knowledge of Vaadin apps owes to another tutorial by
 * the same author: https://vaadin.com/blog/building-a-web-ui-for-mysql-databases-in-plain-java-)
 * More detailed orientation on the use of Vaadin binders is gained from the tutorial by Kirill Bulatov
 * (https://vaadin.com/blog/15624687).
 *
 * Since this small application is meant simply as a very basic initial template for
 * an actual application, however, sign-up and main pages are not added as independent classes.
 * In particular, switch between sign-in and sign-up is made by controlling the visibility
 * of certain components. A mock main page with a few components is encapsulated in the private method
 * loadMockMainUI.
 */
@SpringUI
public class LoginUI extends UI {

    private final UserAuthenticationDAO userAuthenticationDAOSQL;
    private Binder<User> userBinder = new Binder<>();
    private User user = new User();

    @Autowired
    public LoginUI(UserAuthenticationDAO userAuthenticationDAOSQL){

        this.userAuthenticationDAOSQL = userAuthenticationDAOSQL;
    }

    // Sign-in related components
    private TextField userNameTextField = new TextField("User Name");
    private PasswordField passwordTextField = new PasswordField("Password");
    private Button signInButton = new Button("Sign in", e ->  signIn(user));

    // Sign-up related components
    private TextField emailTextField = new TextField("Email");
    private Label newUserLabel = new Label("<span style='cursor: pointer; color:blue'>new user?</span>",
                                           ContentMode.HTML);
    private HorizontalLayout signUpLayout = new HorizontalLayout(newUserLabel);
    private Button signUpButton = new Button("Sign up", e -> signUp(user));

    @Override
    protected void init(VaadinRequest req){

        // Bind the user object to text fields for reading in form inputs
        userBinder.bind(userNameTextField, User::getUserName, User::setUserName);
        userBinder.bind(emailTextField, User::getEmail, User::setEmail);
        userBinder.bind(passwordTextField, User::getPassword, User::setPassword);
        userBinder.setBean(user);

        signInButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        signUpButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // Hide sign-up specific components by visibility for the default(sign-in)
        signUpButton.setVisible(false);
        emailTextField.setVisible(false);

        // Upon click on 'New user?', show sign-up specific components and hide sign-in button.
        // To sign up, all fields required.
        signUpLayout.addLayoutClickListener(e -> {

                newUserLabel.setValue("Sign up now!");
                emailTextField.setVisible(true);
                signInButton.setVisible(false);
                signUpButton.setVisible(true);
                userNameTextField.setRequiredIndicatorVisible(true);
                emailTextField.setRequiredIndicatorVisible(true);
                passwordTextField.setRequiredIndicatorVisible(true);
            }
        );

        FormLayout logInFormLayout = new FormLayout(userNameTextField, emailTextField,
                                                    passwordTextField, signUpLayout,
                                                    signInButton, signUpButton);
        logInFormLayout.setSizeUndefined();

        VerticalLayout logInPageLayout = new VerticalLayout(logInFormLayout);
        logInPageLayout.setSizeFull();

        logInPageLayout.setComponentAlignment(logInFormLayout, Alignment.TOP_CENTER);

        setContent(logInPageLayout);
    }

    /**
     * If the user has valid credentials, show the (mock) main page; else, show the error message
     * @param userRequest User object encapsulating the input form data, namely user_name and password
     */
    private void signIn(User userRequest){

        if(userAuthenticationDAOSQL.checkAuthentication(userRequest)){

            // In actual app, call a main page UI, but here just shows the mock.
            setContent(loadMockMainPage(userAuthenticationDAOSQL.getAuthenticatedUserName()));
        } else{

            Notification.show("Invalid user name or password", Notification.Type.ERROR_MESSAGE);
        }
    }

    /**
     * Register a new user
     * @param userRequest User object encapsulating the input form data, namely user_name, email, and password
     */
    private void signUp(User userRequest){

        try {
            userAuthenticationDAOSQL.addNewUser(userRequest);
            // Upon click on sign-up, redirect to sign-in
            Page.getCurrent().setLocation("/");
        } catch (Exception e){

            Notification.show("Sign up failed: " + e.getMessage(),
                              Notification.Type.ERROR_MESSAGE);
        }
    }

    /**
     * This method encapsulates the mock main page content
     * @param userName parameter added just to show the user's name upon sign-in
     * @return content of the mock main page
     */
    private VerticalLayout loadMockMainPage(String userName){

        Label label = new Label("You are logged in, " + userName);
        // Upon sign-out, redirect to sign-in
        Button signOut = new Button("Sign out", e -> Page.getCurrent().setLocation("/"));

        return new VerticalLayout(label, signOut);
    }
}
