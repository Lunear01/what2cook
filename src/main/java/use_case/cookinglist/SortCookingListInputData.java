package use_case.cookinglist;

public class SortCookingListInputData {
    private final String username;
    private final SortType sortType;

    public enum SortType {
        BY_HEALTH_SCORE,
        BY_CALORIES
    }
//not recorded on github

    public SortCookingListInputData(String username, SortType sortType) {
        // Bug #16 修复: 添加参数验证
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (sortType == null) {
            throw new IllegalArgumentException("SortType cannot be null");
        }
        this.username = username;
        this.sortType = sortType;
    }

    public String getUsername() {
        return username;
    }

    public SortType getSortType() {
        return sortType;
    }
}
