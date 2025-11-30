package use_case.fridge.AddToFridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import dataaccess.IngredientDataAccessInterface;
import entity.Ingredient;
import org.junit.Test;

/**
 * Unit test for AddToFridgeInteractor.
 * Pattern is the same idea as RecipeSearchInteractorTest:
 * fake DAO + fake presenter.
 */
public class AddToFridgeInteractorTest {

    /**
     * Fake DAO that only records the last call parameters.
     */
    private static final class FakeFridgeDao implements IngredientDataAccessInterface {

        String lastUsername;
        int lastIngredientId;
        String lastIngredientName;
        int addCalls;

        @Override
        public void addIngredient(String username, String ingredientName) {
            this.lastUsername = username;
            this.lastIngredientName = ingredientName;
            this.addCalls++;
        }

        @Override
        public void deleteIngredient(String username, int ingredientId) {
            // Not used in this test.
            throw new UnsupportedOperationException("deleteIngredient not used in this test");
        }

        @Override
        public boolean exists(String username, int ingredientID) {
            return false;
        }

        @Override
        public List<Ingredient> getAllIngredients(String username) {
            // Not used in this test.
            throw new UnsupportedOperationException("getAllIngredients not used in this test");
        }
    }

    /**
     * Fake presenter that captures the last success and error.
     */
    private static final class FakePresenter implements AddToFridgeOutputBoundary {

        AddToFridgeResponseModel lastSuccess;
        String lastError;

        @Override
        public AddToFridgeResponseModel prepareSuccessView(AddToFridgeResponseModel responseModel) {
            this.lastSuccess = responseModel;
            return responseModel;
        }

        @Override
        public AddToFridgeResponseModel prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
            return null;
        }
    }

    @Test
    public void addIngredient_callsDaoAndPresenterWithSameValues() throws Exception {
        FakeFridgeDao dao = new FakeFridgeDao();
        FakePresenter presenter = new FakePresenter();
        AddToFridgeInteractor interactor = new AddToFridgeInteractor(dao, presenter);

        AddToFridgeRequestModel request =
                new AddToFridgeRequestModel("alice", 42, "eggs");

        AddToFridgeResponseModel out = interactor.addIngredient(request);

        // DAO should be called once with correct values
        assertEquals(1, dao.addCalls);
        assertEquals("alice", dao.lastUsername);
        assertEquals("eggs", dao.lastIngredientName);

        // Presenter should receive the same response model
        assertNotNull(out);
        assertSame(out, presenter.lastSuccess);
        assertEquals("alice", out.getUserName());
        assertEquals(42, out.getIngredientId());
        assertEquals("eggs", out.getIngredientName());
        assertNull(presenter.lastError);
    }
}
