package use_case.cookinglist;

import entity.Recipe;
import java.util.List;

/**
 * Output data after removing a recipe.
 */
public class RemoveFromCookingListOutputData {

    private final List<Recipe> updatedCookingList;
    private final String message;

    public RemoveFromCookingListOutputData(List<Recipe> updatedCookingList, String message) {
        this.updatedCookingList = updatedCookingList;
        this.message = message;
    }

    public List<Recipe> getUpdatedCookingList() {
        return updatedCookingList;
    }

    public String getMessage() {
        return message;
    }
}
