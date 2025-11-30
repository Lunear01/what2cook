package interface_adapter.cookinglist;

import use_case.cookinglist.SortCookingListInputBoundary;
import use_case.cookinglist.SortCookingListInputData;
import use_case.cookinglist.SortCookingListInputData.SortType;

public class SortCookingListController {
    private final SortCookingListInputBoundary interactor;

    public SortCookingListController(SortCookingListInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * 对指定用户的烹饪列表进行排序
     *
     * @param username 用户名
     * @param sortType 排序类型（按健康分数或卡路里）
     */
    public void sort(String username, SortType sortType) {
        final SortCookingListInputData inputData =
                new SortCookingListInputData(username, sortType);
        interactor.execute(inputData);
    }
}
