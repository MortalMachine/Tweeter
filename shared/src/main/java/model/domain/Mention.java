package model.domain;

import java.io.Serializable;

public class Mention implements StatusItem, Serializable {
    public String alias;

    private Mention() {}
    public Mention(String alias) {
        this.alias = alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return alias;
    }
}
