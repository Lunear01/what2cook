package use_case.cookinglist;

import entity.Recipe;

import java.util.List;

public class SortCookingListOutputData {
    private final List<Recipe> sortedCookingList;
    private final String message;

    public SortCookingListOutputData(List<Recipe> sortedCookingList, String message) {
        this.sortedCookingList = sortedCookingList;
        this.message = message;
    }

//not recorded on github
    public List<Recipe> getSortedCookingList() {
        return sortedCookingList;
    }

    public String getMessage() {
        return message;
    }
}
