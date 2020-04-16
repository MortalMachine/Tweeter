package model.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class User implements Serializable, Comparable<User> {

    public String firstName;
    public String lastName;
    public String alias;
    public String imageUrl;
    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private User() {}

    public User(String alias) {
        this.alias = alias;
    }
    public User(String firstName, String lastName, String alias) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
    }
    public User( String firstName,  String lastName,  String alias, String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.imageUrl = imageUrl;
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAlias(String alias) { this.alias = alias; }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getName() {
        return String.format("%s %s", firstName, lastName);
    }
    public String getAlias() {
        return alias;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (this.alias == null && user.getAlias() != null) return false;
        if (this.alias != null && user.getAlias() == null) return false;
        return alias.equals(user.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }

    @Override
    public int compareTo(User user) {
        return this.getAlias().compareTo(user.getAlias());
    }
}
