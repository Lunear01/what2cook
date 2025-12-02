package use_case.fridge.DeleteFridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import use_case.fridge.IngredientDataAccessInterface;
import entity.Ingredient;
import org.junit.Test;

/**
 * Unit test for DeleteFridgeInteractor.
 * Pattern is the same as AddToFridgeInteractorTest:
 * fake DAO + fake presenter.
 */
public class DeleteFridgeInteractorTest {

    /**
     * Fake DAO that only records the last delete call parameters.
     */
    private static final class FakeFridgeDao implements IngredientDataAccessInterface {

        String lastUsername;
        int lastIngredientId;
        int deleteCalls;

        @Override
        public int addIngredient(String username, String ingredientName) {
            throw new UnsupportedOperationException("addIngredient not used in this test");
        }

        @Override
        public void deleteIngredient(String username, int ingredientId) {
            this.lastUsername = username;
            this.lastIngredientId = ingredientId;
            this.deleteCalls++;
        }

        @Override
        public boolean exists(String username, int ingredientID) {
            return false;
        }

        @Override
        public List<Ingredient> getAllIngredients(String username) {
            throw new UnsupportedOperationException("getAllIngredients not used in this test");
        }
    }

    /**
     * Fake presenter that captures the last success and error.
     */
    private static final class FakePresenter implements DeleteFridgeOutputBoundary {

        DeleteFridgeResponseModel lastSuccess;
        String lastError;

        @Override
        public DeleteFridgeResponseModel prepareSuccessView(DeleteFridgeResponseModel responseModel) {
            this.lastSuccess = responseModel;
            return responseModel;
        }

        @Override
        public DeleteFridgeResponseModel prepareFailViewDelete(String errorMessage) {
            this.lastError = errorMessage;
            return null;
        }
    }

    @Test
    public void deleteIngredient_callsDaoAndPresenterWithSameValues() throws Exception {
        FakeFridgeDao dao = new FakeFridgeDao();
        FakePresenter presenter = new FakePresenter();
        DeleteFridgeInteractor interactor = new DeleteFridgeInteractor(dao, presenter);

        DeleteFridgeRequestModel request =
                new DeleteFridgeRequestModel("alice", 42);

        DeleteFridgeResponseModel out = interactor.deleteIngredient(request);

        // DAO should be called once with correct values
        assertEquals(1, dao.deleteCalls);
        assertEquals("alice", dao.lastUsername);
        assertEquals(42, dao.lastIngredientId);

        // Presenter should receive the same response model.
        assertNotNull(out);
        assertSame(out, presenter.lastSuccess);
        assertEquals("alice", out.getUserName());
        assertEquals(42, out.getIngredientId());
        assertNull(presenter.lastError);
    }
}

