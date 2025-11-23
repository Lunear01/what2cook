package app.cookinglist;

/**
 * Input boundary for removing a recipe from cooking list.
 */
public interface RemoveFromCookingListInputBoundary {
    void execute(RemoveFromCookingListInputData inputData);
}
