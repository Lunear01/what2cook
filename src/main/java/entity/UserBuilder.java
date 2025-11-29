package entity;

import com.sanctionco.jmail.JMail;

public class UserBuilder {
    private String userName;
    private String userPassword;
    private String userEmail;

    /**
     * Set the password for this UserBuilder.
     *
     * @param name the user's name
     * @return this builder
     */
    public UserBuilder withName(String name) {
        this.userName = name;
        return this;
    }

    /**
     * Set the password for this UserBuilder.
     *
     * @param password the user's password
     * @return this builder
     */
    public UserBuilder withPassword(String password) {
        this.userPassword = password;
        return this;
    }

    /**
     * Set the password for this UserBuilder.
     *
     * @param email the user's email
     * @return this builder
     */
    public UserBuilder withEmail(String email) {
        this.userEmail = email;
        return this;
    }

    /**
     * Builds and returns a new User instance.
     *
     * @return the constructed {@code User}.
     * @throws IllegalStateException if the userName is missing,
     *                               if the email is missing,
     *                               or if the email format is invalid.
     */
    public User build() {

        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalStateException("User name is required");
        }

        if (userEmail == null || userEmail.trim().isEmpty()) {
            throw new IllegalStateException("Email is required");
        }
        // Validate email format
        if (!JMail.isValid(userEmail)) {
            throw new IllegalStateException("Invalid email format: " + userEmail);
        }

        return new User(userName, userPassword, userEmail);

    }

}
