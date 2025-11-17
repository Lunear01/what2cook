package entity;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The representation of a password-protected user for our program.
 */
public class User {

    private final String name;
    private final String password;
    private final List<Recipe> personalCookingList;
    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.personalCookingList = new ArrayList<>();
    }

    public String getName() {

        return name;
    }

    public String getPassword() {

        return password;
    }



    public List<Recipe> getPersonalCookingList() {
        return Collections.unmodifiableList(personalCookingList);
        }


    public void addToPersonalCookingList(Recipe recipe) {
        if (recipe != null && !personalCookingList.contains(recipe)){
        personalCookingList.add(recipe);
        }
    }

    public void removeFromPersonalCookingList(Recipe recipe){
            personalCookingList.remove(recipe);
        }
}







