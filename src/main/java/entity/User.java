package entity;

import java.util.Collections;
import java.util.List;

public final class User {

    private final String name;
    private final String password;
    private final String email;
    private final List<Recipe> personalCookingList;
    private final List<Ingredient> refrigerator;

    // Private constructor
    User(String name, String password, String email, List<Recipe> personalCookingList, List<Ingredient> refrigerator) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.personalCookingList = personalCookingList;
        this.refrigerator = refrigerator;
    }

    // Builder instance for User
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    /* ------------------- Getters ------------------- */

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
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
