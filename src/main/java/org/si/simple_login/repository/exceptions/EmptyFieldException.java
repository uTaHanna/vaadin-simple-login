package org.si.simple_login.repository.exceptions;

public class EmptyFieldException extends Exception{

    @Override
    public String getMessage(){

        return "All fields required";
    }
}
