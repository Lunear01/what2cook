package entity;

/* ------------------- Builder ------------------- */
public class UserBuilder {
    private String name;
    private String password;

    /**
     * Sets the name for this UserBuilder.
     *
     * @param name the name of the user
     * @return this builder
     */
    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the password for this UserBuilder.
     *
     * @param password the user's password
     * @return this builder
     */
    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public User build() {
        return new User(this);
    }
    /* ------------------- Getters ------------------- */

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
