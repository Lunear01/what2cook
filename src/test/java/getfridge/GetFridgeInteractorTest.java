package use_case.fridge.GetFridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import dataaccess.IngredientDataAccessInterface;
import entity.Ingredient;
import org.junit.Test;

/**
 * Unit test for GetFridgeInteractor.
 * Follows the same pattern as AddToFridgeInteractorTest and DeleteFridgeInteractorTest:
 * fake DAO + fake presenter.
 */
public class GetFridgeInteractorTest {

    /**
     * Fake DAO that records calls and returns a fixed ingredient list.
     */
    private static final class FakeFridgeDao implements IngredientDataAccessInterface {

        String lastUsername;
        int getCalls;

        @Override
        public int addIngredient(String userName, String ingredientName) {
            throw new UnsupportedOperationException("addIngredient not used in this test");
        }

        @Override
        public List<Ingredient> getAllIngredients(String userName) {
            this.lastUsername = userName;
            this.getCalls++;

            List<Ingredient> list = new ArrayList<>();
            list.add(Ingredient.builder().setId(1).setName("milk").build());
            list.add(Ingredient.builder().setId(2).setName("eggs").build());
            return list;
        }

        @Override
        public void deleteIngredient(String userName, int ingredientID) {
            throw new UnsupportedOperationException("deleteIngredient not used in this test");
        }

        @Override
        public boolean exists(String username, int ingredientID) {
            return false;
        }
    }

    /**
     * Fake presenter that captures the last success and error.
     */
    private static final class FakePresenter implements GetFridgeOutputBoundary {

        GetFridgeResponseModel lastSuccess;
        String lastError;

        @Override
        public GetFridgeResponseModel prepareSuccessView(GetFridgeResponseModel responseModel) {
            this.lastSuccess = responseModel;
            return responseModel;
        }


        @Override
        public GetFridgeResponseModel prepareFailViewGet(String errorMessage) {
            this.lastError = errorMessage;
            return null;
        }
    }

    @Test
    public void getIngredient_callsDaoAndPresenterWithSameValues() throws Exception {
        FakeFridgeDao dao = new FakeFridgeDao();
        FakePresenter presenter = new FakePresenter();
        GetFridgeInteractor interactor = new GetFridgeInteractor(dao, presenter);

        GetFridgeRequestModel request =
                new GetFridgeRequestModel("alice");

        GetFridgeResponseModel out = interactor.getIngredient(request);

        // DAO should be called once and with correct username
        assertEquals(1, dao.getCalls);
        assertEquals("alice", dao.lastUsername);

        // Presenter should receive the same response model
        assertNotNull(out);
        assertSame(out, presenter.lastSuccess);
        assertEquals("alice", out.getUserName());
        assertNotNull(out.getIngredients());
        assertEquals(2, out.getIngredients().size());
        assertEquals("milk", out.getIngredients().get(0).getName());
        assertEquals("eggs", out.getIngredients().get(1).getName());
        assertNull(presenter.lastError);
    }
}

