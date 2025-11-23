package interface_adapter.cookinglist;

import java.util.ArrayList;
import java.util.List;

import entity.Recipe;

public class CookingListState {
    private List<Recipe> personalCookingList = new ArrayList<>();

    // 状态提示信息，比如 “XXX added to your cooking list”
    private String statusMessage = "";

    public List<Recipe> getPersonalCookingList() {
        return personalCookingList;
    }

    public void setPersonalCookingList(List<Recipe> personalCookingList) {
        this.personalCookingList = personalCookingList;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
