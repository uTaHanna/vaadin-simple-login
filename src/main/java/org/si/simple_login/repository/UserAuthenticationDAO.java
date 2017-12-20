package org.si.simple_login.repository;

import org.si.simple_login.domain.User;

/**
 * This interface specifies contracts for an implementing class responsible for user
 * authentication related functionalities.
 */
public interface UserAuthenticationDAO {

    /**
     * Check user credentials.
     * @param userRequest User object encapsulating the input form data, namely user_name and password
     * @return true if the credentials confirmed; else, false
     */
    boolean checkAuthentication(User userRequest);

    /**
     * Insert a new user into the 'user' table; hash the password.
     * @param userRequest User object encapsulating the input form data, namely user_name, email, and password
     * @throws Exception passes messages to display to the front end
     */
    void addNewUser(User userRequest)throws Exception;

    /**
     * Used to get the current user name from UserAuthenticationDAO class.
     * @return the user_name of the user
     */
    String getAuthenticatedUserName();
}
