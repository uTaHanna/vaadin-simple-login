package org.si.simple_login.repository;

import org.si.simple_login.domain.User;

/**
 * This interface specifies contracts for an implementing class responsible for user
 * authentication related functionalities.
 */
public interface UserAuthenticationDAO {

    /**
     * Checks user credentials
     * @param userRequest User object encapsulating the input form data, namely user_name and password
     * @return true if the credentials confirmed; else, false
     */
    boolean checkAuthentication(User userRequest);

    /**
     * Inserts a new user into the 'user' table; hash the password
     * @param userRequest User object encapsulating the input form data, namely user_name, email, and password
     * @throws Exception passes messages to display to the front end
     */
    void addNewUser(User userRequest) throws Exception;

    /**
     * Manages sign-out
     */
    void signOut();
}
