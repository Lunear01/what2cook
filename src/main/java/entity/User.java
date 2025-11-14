package entity;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The representation of a password-protected user for our program.
 */
public class User {

    private final String name;
    private final String password;
    private final List<Recipe> personalCookingList = new ArrayList<>();
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}

public List<Recipe> getPersonalCookingList() {
    return Collections.unmodifiableList(getPersonalCookingList());

}

public void addToPersonalCookingList(Recipe recipe) {
    if (recipe != null { && !personalCookingList.contains(recipe)){
    personalCookinglist.add)(recipe);
    }
}

public void removeFromPersonalCookingList(Recipe recipe) {
    personalCookingList.remove(recipe);
    }