package use_case.cookinglist;

import entity.Recipe;
import entity.User;

public class AddToCookingListInteractor implements AddToCookingListInputBoundary {
    private final UserDataAccessInterface userDao;
    private final AddToCookingListOutputBoundary presenter;

    public AddToCookingListInteractor(UserDataAccessInterface userDao,
                                      AddToCookingListOutputBoundary presenter) {
        this.userDao = userDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddToCookingListInputData inputData) {
        final User user = userDao.get(inputData.getUsername());
        final Recipe recipe = inputData.getRecipe();

//        user.addToPersonalCookingList(recipe);
//        userDao.save(user);
//
//        presenter.present(
//                new AddToCookingListOutputData(
//                        user.getPersonalCookingList(),
//                        recipe.getTitle() + " added to your cooking list!"
//                )
//        );
    }
}
