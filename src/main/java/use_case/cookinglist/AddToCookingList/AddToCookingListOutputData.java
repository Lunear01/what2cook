package use_case.cookinglist.AddToCookingList;

import java.util.List;

import entity.Recipe;

public class AddToCookingListOutputData {
    private final List<Recipe> updatedCookingList;
    private final String message;

    public AddToCookingListOutputData(List<Recipe> updatedCookingList, String message) {
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
