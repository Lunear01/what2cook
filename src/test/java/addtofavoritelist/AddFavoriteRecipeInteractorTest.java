package use_case.add_favorite_list;

import entity.Ingredient;
import entity.Recipe;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

// ----- Tests -----

public class AddFavoriteRecipeInteractorTest {

    // ----- Fakes -----

    private static class FakeFavoritesDao implements AddFavoriteRecipeDataAccessInterface {

        final List<Recipe> favorites = new ArrayList<>();

        int addCalls = 0;
        String lastUsernameAdd;
        Recipe lastAdded;

        @Override
        public List<Recipe> getFavorites(String username) {
            return new ArrayList<>(favorites);
        }

        @Override
        public void addToFavorites(String username, Recipe recipe) {
            addCalls++;
            lastUsernameAdd = username;
            lastAdded = recipe;
            favorites.add(recipe);
        }
    }

    private static class FakeAddFavoritePresenter implements AddFavoriteRecipeOutputBoundary {
        AddFavoriteRecipeOutputData lastOutput;

        @Override
        public void present(AddFavoriteRecipeOutputData data) {
            this.lastOutput = data;
        }
    }

    // ----- Test fields -----

    private FakeFavoritesDao dao;
    private FakeAddFavoritePresenter presenter;
    private AddFavoriteRecipeInteractor interactor;

    @Before
    public void setUp() {
        dao = new FakeFavoritesDao();
        presenter = new FakeAddFavoritePresenter();
        interactor = new AddFavoriteRecipeInteractor(dao, presenter);
    }

    private Recipe buildRecipe(int id, String title) {
        return Recipe.builder()
                .setId(id)
                .setTitle(title)
                .setImage(title.toLowerCase() + ".png")
                .setHealthScore(80)
                .setCalories(300)
                .setIngredientNames(Collections.singletonList(
                        Ingredient.builder().setName("ing").setId(id).build()
                ))
                .setInstructions("Cook " + title)
                .build();
    }

    /**
     * Last message should be null before any execute() calls.
     */
    @Test
    public void getLastMessage_beforeExecute_returnsNull() {
        assertNull(interactor.getLastMessage());
    }

    @Test
    public void execute_addNewFavorite_addsAndPresentsUpdatedList() {
        Recipe recipe = buildRecipe(1, "Pizza");
        AddFavoriteRecipeInputData input =
                new AddFavoriteRecipeInputData("user", recipe);

        interactor.execute(input);

        AddFavoriteRecipeOutputData out = presenter.lastOutput;
        assertNotNull(out);
        assertEquals("Pizza added to your favorites!", out.getMessage());
        assertEquals(1, out.getFavorites().size());
        assertEquals(1, out.getFavorites().get(0).getId());

        assertEquals(1, dao.addCalls);
        assertEquals("user", dao.lastUsernameAdd);
        assertEquals(recipe, dao.lastAdded);

        assertEquals("Pizza added to your favorites!", interactor.getLastMessage());
    }

    @Test
    public void execute_addDuplicateFavorite_doesNotCallAddAndShowsAlreadyMessage() {
        Recipe existing = buildRecipe(2, "Burger");
        dao.favorites.add(existing);

        Recipe duplicate = buildRecipe(2, "Burger"); // different object, same id
        AddFavoriteRecipeInputData input =
                new AddFavoriteRecipeInputData("user", duplicate);

        interactor.execute(input);

        AddFavoriteRecipeOutputData out = presenter.lastOutput;
        assertNotNull(out);
        assertEquals("Burger is already in your favorites.", out.getMessage());
        assertEquals(1, out.getFavorites().size());
        assertEquals(0, dao.addCalls);

        assertEquals("Burger is already in your favorites.", interactor.getLastMessage());
    }
}
