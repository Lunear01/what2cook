package entity;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public final class User {

    private final String name;
    private final String password;
    private final List<Recipe> personalCookingList;
    private final List<Ingredient> refrigerator;

    protected User(UserBuilder builder) {
        this.name = builder.getName();
        this.password = builder.getPassword();
        this.personalCookingList = new ArrayList<>();
        this.refrigerator = new ArrayList<>();
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