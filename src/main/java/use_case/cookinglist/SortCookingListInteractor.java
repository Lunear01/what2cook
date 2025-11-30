package use_case.cookinglist;

import entity.Recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortCookingListInteractor implements SortCookingListInputBoundary {

    //not recorded on github
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

        // 获取当前的烹饪列表
        List<Recipe> currentList;
        try {
            currentList = cookingListDao.getAllRecipes(username);
        } catch (RuntimeException e) {
            // 用户第一次使用，还没有 cooking list，使用空列表
            currentList = new ArrayList<>();
        }

        // 创建副本进行排序
        final List<Recipe> sortedList = new ArrayList<>(currentList);

        // 根据排序类型进行排序
        String message;
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

        // 通过 presenter 呈现结果
        presenter.present(new SortCookingListOutputData(sortedList, message));
    }
}
