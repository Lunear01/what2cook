package use_case.cookinglist.GetCookingList;

/**
 * Input data for getting the cooking list.
 */
public class GetCookingListInputData {

    private final String username;

    public GetCookingListInputData(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
