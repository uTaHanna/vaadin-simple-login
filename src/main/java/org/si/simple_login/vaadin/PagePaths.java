package org.si.simple_login.vaadin;

public enum PagePaths {

    LOGIN(""),
    SIGNUP("sign_up"),
    MAIN("main");

    private String pagePath;

    PagePaths(String pagePath) {

        this.pagePath = pagePath;
    }

    public String getPagePath(){

        return pagePath;
    }
}
