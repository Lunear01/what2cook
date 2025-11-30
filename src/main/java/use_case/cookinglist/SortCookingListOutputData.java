package use_case.cookinglist;

import java.util.List;

import entity.Recipe;

/**
 * Output data for sorting the cooking list.
 */
public class SortCookingListOutputData {
    private final List<Recipe> sortedCookingList;
    private final String message;

    /**
     * Constructor for SortCookingListOutputData.
     * @param sortedCookingList the sorted list of recipes
     * @param message the message describing the sort result
     */
    public SortCookingListOutputData(List<Recipe> sortedCookingList, String message) {
        this.sortedCookingList = sortedCookingList;
        this.message = message;
    }

    public List<Recipe> getSortedCookingList() {
        return sortedCookingList;
    }

    public String getMessage() {
        return message;
    }
}
