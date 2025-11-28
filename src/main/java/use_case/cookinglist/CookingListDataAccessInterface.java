package use_case.cookinglist;

import java.util.List;
import entity.Recipe;

public interface CookingListDataAccessInterface {


    List<Recipe> getCookingList(String username);

    void addToCookingList(String username, Recipe recipe);
}
