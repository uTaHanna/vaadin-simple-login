package org.si.simple_login.vaadin;

// This class stores the names of the paths to views at the single location.
public enum ViewPaths {

    LOGIN(""),
    SIGN_UP("/sign_up"),
    MAIN("/main");

    private String pagePath;

    ViewPaths(String pagePath) {

        this.pagePath = pagePath;
    }

    public String getPagePath(){

        return pagePath;
    }
}
