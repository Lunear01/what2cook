package use_case.cookinglist.cookinglist;
import entity.Recipe;
import entity.User;
import java.util.List;


public class AddToCookingListInteractor implements AddToCookingListInputBoundary {
    private final UserDataAccessInterface userDAO;
    private final AddToCookingListOutputBoundary presenter;

    public AddToCookingListInteractor(UserDataAccessInterface userDAO,
                                      AddToCookingListOutputBoundary presenter) {
        this.userDAO = userDAO;
        this.presenter = presenter;
    }

    @Override
    public void add(String username, Recipe recipe) {
        User user = userDAO.getUser(username);
        if (user == null) {
            presenter.prepareFailView("User not found: " + username);
            return;
        }

        // ✅ 真正往 personalCookingList 里加
        user.addToPersonalCookingList(recipe);

        // 保存回 DAO
        userDAO.saveUser(user);

        // 把最新列表给 Presenter
        List<Recipe> updatedList = user.getPersonalCookingList();
        AddToCookingListOutputData outputData =
                new AddToCookingListOutputData(updatedList);

        presenter.prepareSuccessView(outputData);
    }
    @Override
    public void remove(String username, Recipe recipe) {
        User user = userDAO.getUser(username);
        if (user == null) {
            presenter.prepareFailView("User not found: " + username);
            return;
        }
        user.removeFromPersonalCookingList(recipe);
        userDAO.saveUser(user);

        List<Recipe> updatedList = user.getPersonalCookingList();
        AddToCookingListOutputData data =
                new AddToCookingListOutputData(updatedList);
        presenter.prepareSuccessView(data);
    }
}