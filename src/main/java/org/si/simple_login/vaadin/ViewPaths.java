package org.si.simple_login.vaadin;

// This class stores the names of the paths to views at the single location.
public enum ViewPaths {

    LOGIN(""),
    SIGN_UP("sign_up"),
    MAIN("main");

    private String viewPath;

    ViewPaths(String viewPath) {

        this.viewPath = viewPath;
    }

    public String getViewPath(){

        return viewPath;
    }
}
