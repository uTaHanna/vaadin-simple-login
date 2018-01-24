package org.si.simple_login.vaadin;

public enum PagePaths {

    MAIN_PAGE("main_page");

    private String pagePath;

    PagePaths(String pagePath) {

        this.pagePath = pagePath;
    }

    public String getPagePath(){

        return pagePath;
    }
}
