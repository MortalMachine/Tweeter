package model.domain;

import java.io.Serializable;
import java.util.UUID;

public class AuthToken implements Serializable {
    public static long TWO_HRS_IN_MS = 7200000;
    public String id;
    public String alias;
    public long expMs;

    private AuthToken() {}
    public AuthToken(String alias) {
        id = UUID.randomUUID().toString();
        this.alias = alias;
        this.setExpMs();
    }

    public void setExpMs() {
        this.expMs = System.currentTimeMillis() + TWO_HRS_IN_MS;
    }
    public String getId() {
        return id;
    }
    public String getAlias() {
        return alias;
    }
    public long getExpMs() {
        return expMs;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        AuthToken other = (AuthToken) o;
        if (other.getId().equals(this.getId()) &&
            other.getAlias().equals(this.getAlias())) {
            return true;
        }
        return false;
    }
}
