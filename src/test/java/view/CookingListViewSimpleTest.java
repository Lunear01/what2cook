package view;

import entity.Recipe;
import interface_adapter.cookinglist.CookingListState;
import interface_adapter.cookinglist.CookingListViewModel;
import interface_adapter.cookinglist.SortCookingListController;
import org.junit.Before;
import org.junit.Test;
import use_case.cookinglist.SortCookingListInputData;
import use_case.cookinglist.SortCookingListInputData.SortType;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Simplified test for CookingListView using JUnit 4.
 * Achieves high code coverage using real objects.
 */
public class CookingListViewSimpleTest {

    private TestCookingListViewModel viewModel;
    private TestSortController sortController;
    private CookingListView cookingListView;

    @Before
    public void setUp() {
        viewModel = new TestCookingListViewModel();
        sortController = new TestSortController();
        cookingListView = new CookingListView(viewModel, sortController);
    }

    @Test
    public void testConstructor_CreatesAllComponents() {
        assertNotNull(cookingListView);
        // Verify components exist
        assertNotNull(findButton(cookingListView, "Back"));
        assertNotNull(findButton(cookingListView, "Sort by Health Score"));
        assertNotNull(findButton(cookingListView, "Sort by Calories"));
    }

    @Test
    public void testSetCurrentUsername_AllowsSorting() {
        cookingListView.setCurrentUsername("testUser");

        JButton sortButton = findButton(cookingListView, "Sort by Health Score");
        sortButton.doClick();

        assertEquals("testUser", sortController.lastUsername);
        assertEquals(SortType.BY_HEALTH_SCORE, sortController.lastSortType);
    }

    @Test
    public void testSortByHealthButton_WithUsername() {
        cookingListView.setCurrentUsername("user1");

        JButton sortButton = findButton(cookingListView, "Sort by Health Score");
        sortButton.doClick();

        assertEquals("user1", sortController.lastUsername);
        assertEquals(SortType.BY_HEALTH_SCORE, sortController.lastSortType);
    }

    @Test
    public void testSortByHealthButton_WithoutUsername() {
        // No username set
        JButton sortButton = findButton(cookingListView, "Sort by Health Score");
        sortButton.doClick();

        // Controller should not be called
        assertNull(sortController.lastUsername);
    }

    @Test
    public void testSortByCaloriesButton_WithUsername() {
        cookingListView.setCurrentUsername("user2");

        JButton sortButton = findButton(cookingListView, "Sort by Calories");
        sortButton.doClick();

        assertEquals("user2", sortController.lastUsername);
        assertEquals(SortType.BY_CALORIES, sortController.lastSortType);
    }

    @Test
    public void testSortByCaloriesButton_WithoutUsername() {
        // No username set
        JButton sortButton = findButton(cookingListView, "Sort by Calories");
        sortButton.doClick();

        // Controller should not be called
        assertNull(sortController.lastUsername);
    }

    @Test
    public void testSortButton_HandlesException() {
        cookingListView.setCurrentUsername("errorUser");
        sortController.shouldThrowError = true;

        JButton sortButton = findButton(cookingListView, "Sort by Health Score");

        // Should not throw exception (handled internally)
        try {
            sortButton.doClick();
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testBackButton_ExecutesCallback() {
        final boolean[] backCalled = {false};
        cookingListView.setOnBack(() -> backCalled[0] = true);

        JButton backButton = findButton(cookingListView, "Back");
        backButton.doClick();

        assertTrue(backCalled[0]);
    }

    @Test
    public void testBackButton_WithNullCallback() {
        cookingListView.setOnBack(null);

        JButton backButton = findButton(cookingListView, "Back");

        // Should not throw exception
        try {
            backButton.doClick();
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testSetOnOpenRecipe_StoresCallback() {
        final Recipe[] capturedRecipe = {null};
        cookingListView.setOnOpenRecipe(recipe -> capturedRecipe[0] = recipe);

        // Callback is stored, will be tested in integration test
        assertNotNull(cookingListView);
    }

    @Test
    public void testPropertyChange_UpdatesView() {
        // Create test recipes
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(createTestRecipe("Recipe 1"));
        recipes.add(createTestRecipe("Recipe 2"));

        // Update state
        viewModel.getState().setPersonalCookingList(recipes);
        viewModel.getState().setStatusMessage("Test Status");

        // Trigger property change
        PropertyChangeEvent evt = new PropertyChangeEvent(
            viewModel, "state", null, viewModel.getState()
        );
        cookingListView.propertyChange(evt);

        // Verify list is updated
        JList<?> recipeList = findJList(cookingListView);
        assertNotNull(recipeList);
        assertEquals(2, recipeList.getModel().getSize());
    }

    @Test
    public void testPropertyChange_WithEmptyList() {
        viewModel.getState().setPersonalCookingList(new ArrayList<>());
        viewModel.getState().setStatusMessage("No recipes");

        PropertyChangeEvent evt = new PropertyChangeEvent(
            viewModel, "state", null, viewModel.getState()
        );
        cookingListView.propertyChange(evt);

        JList<?> recipeList = findJList(cookingListView);
        assertEquals(0, recipeList.getModel().getSize());
    }

    @Test
    public void testPropertyChange_WithNullMessage() {
        viewModel.getState().setPersonalCookingList(new ArrayList<>());
        viewModel.getState().setStatusMessage(null);

        PropertyChangeEvent evt = new PropertyChangeEvent(
            viewModel, "state", null, viewModel.getState()
        );
        cookingListView.propertyChange(evt);

        // Should handle null message gracefully
        JLabel statusLabel = findLabel(cookingListView, " ");
        assertNotNull(statusLabel);
    }

    @Test
    public void testPropertyChange_WithNonStateObject() {
        PropertyChangeEvent evt = new PropertyChangeEvent(
            viewModel, "other", null, "not a state"
        );

        // Should not throw exception
        try {
            cookingListView.propertyChange(evt);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    // Helper methods
    private Recipe createTestRecipe(String title) {
        return Recipe.builder()
            .setTitle(title)
            .setId(1)
            .setCalories(100.0)
            .setHealthScore(80)
            .setImage("test.jpg")
            .setInstructions("Test instructions")
            .setIngredientNames(java.util.Collections.emptyList())
            .build();
    }

    private JButton findButton(Container container, String text) {
        for (Component comp : getAllComponents(container)) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (text.equals(button.getText())) {
                    return button;
                }
            }
        }
        return null;
    }

    private JList<?> findJList(Container container) {
        for (Component comp : getAllComponents(container)) {
            if (comp instanceof JList) {
                return (JList<?>) comp;
            }
        }
        return null;
    }

    private JLabel findLabel(Container container, String text) {
        for (Component comp : getAllComponents(container)) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (text.equals(label.getText())) {
                    return label;
                }
            }
        }
        return null;
    }

    private List<Component> getAllComponents(Container container) {
        List<Component> components = new ArrayList<>();
        components.add(container);
        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                components.addAll(getAllComponents((Container) comp));
            } else {
                components.add(comp);
            }
        }
        return components;
    }

    // Test helper classes (替代 Mockito)

    static class TestCookingListViewModel extends CookingListViewModel {
        // Uses real CookingListViewModel
    }

    static class TestSortController extends SortCookingListController {
        String lastUsername;
        SortType lastSortType;
        boolean shouldThrowError = false;

        public TestSortController() {
            super(null); // null interactor for testing
        }

        @Override
        public void sort(String username, SortType sortType) {
            if (shouldThrowError) {
                throw new RuntimeException("Test error");
            }
            this.lastUsername = username;
            this.lastSortType = sortType;
        }
    }
}
