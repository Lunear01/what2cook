package use_case.cookinglist;

import entity.Recipe;
import entity.User;

import java.util.List;

/**
 * Interactor for removing a recipe from personal cooking list.
 */
public class RemoveFromCookingListInteractor implements RemoveFromCookingListInputBoundary {

    private final UserDataAccessInterface userDao;
    private final RemoveFromCookingListOutputBoundary presenter;

    public RemoveFromCookingListInteractor(UserDataAccessInterface userDao,
                                           RemoveFromCookingListOutputBoundary presenter) {
        this.userDao = userDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(RemoveFromCookingListInputData inputData) {
        if (inputData == null) {
            presenter.present(new RemoveFromCookingListOutputData(
                    List.of(), "Remove failed: input is null."
            ));
            return;
        }

        User user = userDao.getUser(inputData.getUsername());
        Recipe target = inputData.getRecipe();

        if (user == null || target == null) {
            presenter.present(new RemoveFromCookingListOutputData(
                    List.of(), "Remove failed: user or recipe not found."
            ));
            return;
        }

        List<Recipe> list = user.getPersonalCookingList();

        // 按 id 删除（更稳）
        boolean removed = list.removeIf(r -> r.getId() == target.getId());

        userDao.saveUser(user);

        String msg = removed
                ? target.getTitle() + " removed from your cooking list!"
                : target.getTitle() + " was not in your cooking list.";

        presenter.present(new RemoveFromCookingListOutputData(
                user.getPersonalCookingList(),
                msg
        ));
    }
}
