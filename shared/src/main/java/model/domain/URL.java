package model.domain;

import java.io.Serializable;

public class URL implements StatusItem, Serializable {
    public String pathname;

    private URL() {}
    public URL(String pathname) {
        this.pathname = pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

}
