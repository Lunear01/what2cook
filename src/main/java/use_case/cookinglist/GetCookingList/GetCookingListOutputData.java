package use_case.cookinglist.GetCookingList;

import java.util.List;

import entity.Recipe;

/**
 * Output data for getting the cooking list.
 */
public class GetCookingListOutputData {
    private final List<Recipe> cookingList;
    private final String message;

    public GetCookingListOutputData(List<Recipe> cookingList, String message) {
        this.cookingList = cookingList;
        this.message = message;
    }

    public List<Recipe> getCookingList() {
        return cookingList;
    }

    public String getMessage() {
        return message;
    }
}
