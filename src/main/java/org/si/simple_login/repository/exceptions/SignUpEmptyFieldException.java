package org.si.simple_login.repository.exceptions;

public class SignUpEmptyFieldException extends Exception{

    @Override
    public String getMessage(){

        return "All fields required";
    }
}
