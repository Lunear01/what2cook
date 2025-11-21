package use_case.cookinglist.cookinglist;

import entity.Recipe;
public interface AddToCookingListInputBoundary {
    void add(String username, Recipe recipe);
    void remove(String username, Recipe recipe);
}
