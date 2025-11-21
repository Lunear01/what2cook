package entity;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public final class User {

    private final String name;
    private final String password;
    private final List<Recipe> personalCookingList;
    private final List<Ingredient> refrigerator;

    private User(UserBuilder builder) {
        this.name = builder.name;
        this.password = builder.password;
        this.personalCookingList = new ArrayList<>();
        this.refrigerator = new ArrayList<>();
    }

    /* ------------------- Builder ------------------- */
    public static class UserBuilder {
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

    public List<Ingredient> getRefrigerator() {
        return Collections.unmodifiableList(refrigerator);
    }

    /* ------------------- Modify list ------------------- */

    public void addToPersonalCookingList(Recipe recipe) {
        if (recipe != null && !personalCookingList.contains(recipe)) {
            personalCookingList.add(recipe);
        }
    }

    public void addToRefrigerator(Ingredient ingredient) {
        if (!refrigerator.contains(ingredient)) {
            refrigerator.add(ingredient);
        }
    }

    public void removeFromPersonalCookingList(Recipe recipe) {
        personalCookingList.remove(recipe);
    }
}