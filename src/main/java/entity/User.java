package entity;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public class User {

    private final String name;
    private final String password;
    private final List<Recipe> personalCookingList;

    private User(UserBuilder builder) {
        this.name = builder.name;
        this.password = builder.password;
        this.personalCookingList = new ArrayList<>();
    }

    /* ------------------- Builder ------------------- */
    public static class UserBuilder {
        private String name;
        private String password;

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    /* ------------------- Getters ------------------- */

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<Recipe> getPersonalCookingList() {
        return Collections.unmodifiableList(personalCookingList);
    }

    /* ------------------- Modify personal cooking list ------------------- */

    public void addToPersonalCookingList(Recipe recipe) {
        if (recipe != null && !personalCookingList.contains(recipe)) {
            personalCookingList.add(recipe);
        }
    }

    public void removeFromPersonalCookingList(Recipe recipe) {
        personalCookingList.remove(recipe);
    }
}