package use_case.cookinglist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import entity.Recipe;

/**
 * Interactor for sorting the cooking list.
 */
public class SortCookingListInteractor implements SortCookingListInputBoundary {
    private final RecipeDataAccessInterface cookingListDao;
    private final SortCookingListOutputBoundary presenter;

    public SortCookingListInteractor(RecipeDataAccessInterface cookingListDao,
                                     SortCookingListOutputBoundary presenter) {
        this.cookingListDao = cookingListDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(SortCookingListInputData inputData) {
        final String username = inputData.getUsername();
        final SortCookingListInputData.SortType sortType = inputData.getSortType();
        final List<Recipe> currentList = getCurrentList(username);

        // 创建副本进行排序
        final List<Recipe> sortedList = new ArrayList<>(currentList);

        // 根据排序类型进行排序
        final String message = sortRecipes(sortedList, sortType);

        presenter.present(new SortCookingListOutputData(sortedList, message));
    }

    /**
     * Get the current recipe list for the user.
     * @param username the username
     * @return the list of recipes, or empty list if none exists
     */
    private List<Recipe> getCurrentList(String username) {
        try {
            return cookingListDao.getAllRecipes(username);
        }
        catch (RuntimeException exception) {
            // 用户第一次使用，还没有 cooking list，使用空列表
            return new ArrayList<>();
        }
    }

    /**
     * Sort the recipe list based on the sort type.
     * @param sortedList the list to sort
     * @param sortType the type of sort to apply
     * @return the message describing the sort
     */
    private String sortRecipes(List<Recipe> sortedList, SortCookingListInputData.SortType sortType) {
        final String message;
        switch (sortType) {
            case BY_HEALTH_SCORE:
                sortedList.sort(Comparator.comparingInt(Recipe::getHealthScore).reversed());
                message = "Sorted by health score (highest first)";
                break;
            case BY_CALORIES:
                sortedList.sort(Comparator.comparingDouble(Recipe::getCalories));
                message = "Sorted by calories (lowest first)";
                break;
            default:
                message = "Unknown sort type";
                break;
        }
        return message;
    }
}