package interface_adapter.cookinglist;

import interface_adapter.ViewModel;
import entity.Recipe;

import java.util.List;

public class CookingListViewModel extends ViewModel<CookingListState> {

    public static final String VIEW_NAME = "cooking list";

    public CookingListViewModel() {
        super(VIEW_NAME);
        // 初始化 state，避免一开始 getState() 为 null
        this.setState(new CookingListState());
    }

    // 方便 Presenter / View 使用的封装方法：

    public void setPersonalCookingList(List<Recipe> recipes) {
        CookingListState state = getState();
        state.setPersonalCookingList(recipes);
        setState(state);                 // 更新状态
        firePropertyChanged("cooking");  // 通知监听者（比如 NoteView）
    }

    public List<Recipe> getPersonalCookingList() {
        return getState().getPersonalCookingList();
    }

    public void setStatusMessage(String message) {
        CookingListState state = getState();
        state.setStatusMessage(message);
        setState(state);
        firePropertyChanged("statusMessage");
    }

    public String getStatusMessage() {
        return getState().getStatusMessage();
    }
}
