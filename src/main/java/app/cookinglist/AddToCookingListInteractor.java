package app.cookinglist;
import entity.Recipe;
import entity.User;



public class AddToCookingListInteractor {
    private final UserDataAccessInterface userDao;
    private final AddToCookingListOutputBoundary presenter;

    public AddToCookingListInteractor(UserDataAccessInterface userDao,
                                      AddToCookingListOutputBoundary presenter) {
        this.userDao = userDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddToCookingListInputData inputData) {
        User user = userDao.getUser(inputData.getUsername());
        Recipe recipe = inputData.getRecipe();

        user.addToPersonalCookingList(recipe);
        userDao.saveUser(user);

        presenter.present(
                new AddToCookingListOutputData(
                        user.getPersonalCookingList(),
                        recipe.getTitle() + " added to your cooking list!"
                )
        );
    }
}
