package use_case.cookinglist.cookinglist;
import entity.Recipe;
import java.util.List;

public class AddToCookingListOutputData {
    private final List<Recipe> updatedCookingList;


    public AddToCookingListOutputData(List<Recipe> updatedCookingList) {
        this.updatedCookingList = updatedCookingList;

    }
    public List<Recipe> getPersonalCookingList() {
        return updatedCookingList;
    }

    public List<Recipe> getUpdatedCookingList() {
        return updatedCookingList;
    }


}
