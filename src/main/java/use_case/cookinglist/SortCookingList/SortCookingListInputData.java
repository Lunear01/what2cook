package use_case.cookinglist.SortCookingList;

/**
 * Input data for sorting the cooking list.
 */
public class SortCookingListInputData {
    private final String username;
    private final SortType sortType;

    /**
     * Enum representing the type of sort to apply.
     */
    public enum SortType {
        BY_HEALTH_SCORE,
        BY_CALORIES
    }
    // not recorded on github

    /**
     * Constructor for SortCookingListInputData.
     * @param username the username of the current user
     * @param sortType the type of sort to apply
     * @throws IllegalArgumentException if username is null/empty or sortType is null
     */
    public SortCookingListInputData(String username, SortType sortType) {
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
