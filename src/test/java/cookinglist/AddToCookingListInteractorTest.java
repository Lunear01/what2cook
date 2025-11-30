package use_case.cookinglist;

import entity.Ingredient;
import entity.Recipe;
import org.junit.Before;
import org.junit.Test;
import use_case.cookinglist.AddToCookingList.AddToCookingListInputData;
import use_case.cookinglist.AddToCookingList.AddToCookingListInteractor;
import use_case.cookinglist.AddToCookingList.AddToCookingListOutputBoundary;
import use_case.cookinglist.AddToCookingList.AddToCookingListOutputData;
import use_case.cookinglist.SortCookingList.SortCookingListInputData;
import use_case.cookinglist.SortCookingList.SortCookingListInteractor;
import use_case.cookinglist.SortCookingList.SortCookingListOutputBoundary;
import use_case.cookinglist.SortCookingList.SortCookingListOutputData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Fake presenter for testing AddToCookingListInteractor.
 * Captures the last output data for verification in tests.
 */
class FakeAddToCookingListPresenter implements AddToCookingListOutputBoundary {
    private AddToCookingListOutputData lastOutput;

    @Override
    public void present(AddToCookingListOutputData outputData) {
        this.lastOutput = outputData;
    }

    public AddToCookingListOutputData getLastOutput() {
        return lastOutput;
    }
}

/**
 * Fake presenter for testing SortCookingListInteractor.
 * Captures the last output data for verification in tests.
 */
class FakeSortCookingListPresenter implements SortCookingListOutputBoundary {
    private SortCookingListOutputData lastOutput;

    @Override
    public void present(SortCookingListOutputData outputData) {
        this.lastOutput = outputData;
    }

    public SortCookingListOutputData getLastOutput() {
        return lastOutput;
    }
}

/**
 * Fake data access object for testing both interactors.
 * Tracks method calls and stores data in memory.
 */
class FakeRecipeDataAccess implements RecipeDataAccessInterface {

    private final List<Recipe> cookingList = new ArrayList<>();
    private int addRecipeCalls = 0;
    private String lastUsername;
    private Recipe lastAddedRecipe;
    private boolean shouldThrowException = false;

    @Override
    public void addRecipe(String username, Recipe recipe) {
        cookingList.add(recipe);
        addRecipeCalls++;
        lastUsername = username;
        lastAddedRecipe = recipe;
    }

    @Override
    public List<Recipe> getAllRecipes(String username) {
        if (shouldThrowException) {
            throw new RuntimeException("User has no cooking list yet");
        }
        return new ArrayList<>(cookingList);
    }

    @Override
    public void deleteRecipe(String username, int recipeID) {
        cookingList.removeIf(recipe -> recipe.getId() == recipeID);
    }

    // Helpers for tests

    public void addRecipeDirectly(Recipe recipe) {
        cookingList.add(recipe);
    }

    public int getAddRecipeCalls() {
        return addRecipeCalls;
    }

    public String getLastUsername() {
        return lastUsername;
    }

    public Recipe getLastAddedRecipe() {
        return lastAddedRecipe;
    }

    public void setShouldThrowException(boolean shouldThrowException) {
        this.shouldThrowException = shouldThrowException;
    }
}

/**
 * Test suite for AddToCookingListInteractor, SortCookingListInteractor,
 * and their related Input/Output data classes.
 */
public class AddToCookingListInteractorTest {

    private FakeRecipeDataAccess dao;
    private FakeAddToCookingListPresenter presenter;
    private AddToCookingListInteractor interactor;

    @Before
    public void setUp() {
        dao = new FakeRecipeDataAccess();
        presenter = new FakeAddToCookingListPresenter();
        interactor = new AddToCookingListInteractor(dao, presenter);
    }

    @Test
    public void executeAddNewRecipeSuccess() {
        Recipe newRecipe = Recipe.builder()
                .setId(100)
                .setTitle("Chicken Curry")
                .setImage("chicken.png")
                .setHealthScore(85)
                .setCalories(450)
                .setIngredientNames(Collections.singletonList(
                        Ingredient.builder().setName("chicken").setId(1).build()
                ))
                .setInstructions("1. Cook chicken\n2. Add curry")
                .build();

        AddToCookingListInputData input =
                new AddToCookingListInputData("testuser", newRecipe);

        interactor.execute(input);

        AddToCookingListOutputData out = presenter.getLastOutput();
        assertNotNull(out);
        assertEquals("Chicken Curry added to your cooking list!", out.getMessage());
        assertEquals(1, out.getUpdatedCookingList().size());
        assertEquals(100, out.getUpdatedCookingList().get(0).getId());
        assertEquals("Chicken Curry", out.getUpdatedCookingList().get(0).getTitle());

        assertEquals(1, dao.getAddRecipeCalls());
        assertEquals("testuser", dao.getLastUsername());
        assertEquals(newRecipe, dao.getLastAddedRecipe());
    }

    @Test
    public void executeAddDuplicateRecipeShowsAlreadyExistsMessage() {
        Recipe existingRecipe = Recipe.builder()
                .setId(200)
                .setTitle("Pasta Carbonara")
                .setImage("pasta.png")
                .setHealthScore(70)
                .setCalories(600)
                .setIngredientNames(Collections.singletonList(
                        Ingredient.builder().setName("pasta").setId(2).build()
                ))
                .setInstructions("1. Boil pasta\n2. Add sauce")
                .build();

        // prepopulate cooking list
        dao.addRecipeDirectly(existingRecipe);

        AddToCookingListInputData input =
                new AddToCookingListInputData("testuser", existingRecipe);

        interactor.execute(input);

        AddToCookingListOutputData out = presenter.getLastOutput();
        assertNotNull(out);
        assertEquals("Pasta Carbonara is already in your cooking list.", out.getMessage());
        assertEquals(1, out.getUpdatedCookingList().size());
        assertEquals(0, dao.getAddRecipeCalls());
    }

    @Test
    public void executeAddMultipleRecipesSuccess() {
        Recipe recipe1 = Recipe.builder()
                .setId(301)
                .setTitle("Salad")
                .setImage("salad.png")
                .setHealthScore(95)
                .setCalories(200)
                .setIngredientNames(Collections.singletonList(
                        Ingredient.builder().setName("lettuce").setId(3).build()
                ))
                .build();

        Recipe recipe2 = Recipe.builder()
                .setId(302)
                .setTitle("Soup")
                .setImage("soup.png")
                .setHealthScore(80)
                .setCalories(150)
                .setIngredientNames(Collections.singletonList(
                        Ingredient.builder().setName("tomato").setId(4).build()
                ))
                .build();

        interactor.execute(new AddToCookingListInputData("testuser", recipe1));
        interactor.execute(new AddToCookingListInputData("testuser", recipe2));

        AddToCookingListOutputData out = presenter.getLastOutput();
        assertNotNull(out);
        assertEquals("Soup added to your cooking list!", out.getMessage());
        assertEquals(2, out.getUpdatedCookingList().size());
        assertEquals(2, dao.getAddRecipeCalls());
    }

    @Test
    public void executeAddRecipeWithSameIdButDifferentObjectTreatedAsDuplicate() {
        Recipe original = Recipe.builder()
                .setId(400)
                .setTitle("Original Recipe")
                .setImage("original.png")
                .build();
        dao.addRecipeDirectly(original);

        Recipe duplicate = Recipe.builder()
                .setId(400)
                .setTitle("Modified Recipe")
                .setImage("modified.png")
                .build();

        AddToCookingListInputData input =
                new AddToCookingListInputData("testuser", duplicate);

        interactor.execute(input);

        AddToCookingListOutputData out = presenter.getLastOutput();
        assertNotNull(out);
        assertTrue(out.getMessage().contains("is already in your cooking list"));
        assertEquals(1, out.getUpdatedCookingList().size());
        assertEquals("Original Recipe", out.getUpdatedCookingList().get(0).getTitle());
        assertEquals(0, dao.getAddRecipeCalls());
    }

    // These three tests are for AddToCookingListInputData constructor validation

    @Test(expected = IllegalArgumentException.class)
    public void constructInputData_nullUsername_throwsException() {
        Recipe recipe = Recipe.builder()
                .setId(600)
                .setTitle("Test Recipe")
                .build();

        new AddToCookingListInputData(null, recipe);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructInputData_emptyUsername_throwsException() {
        Recipe recipe = Recipe.builder()
                .setId(700)
                .setTitle("Test Recipe")
                .build();

        new AddToCookingListInputData("", recipe);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructInputData_nullRecipe_throwsException() {
        new AddToCookingListInputData("testuser", null);
    }

    // ========== SortCookingListInteractor Tests ==========

    @Test
    public void executeSortByHealthScoreSuccess() {
        // Arrange: Create recipes with different health scores
        Recipe recipe1 = Recipe.builder()
                .setId(1)
                .setTitle("Salad")
                .setHealthScore(95)
                .setCalories(200)
                .build();

        Recipe recipe2 = Recipe.builder()
                .setId(2)
                .setTitle("Pizza")
                .setHealthScore(60)
                .setCalories(800)
                .build();

        Recipe recipe3 = Recipe.builder()
                .setId(3)
                .setTitle("Smoothie")
                .setHealthScore(85)
                .setCalories(150)
                .build();

        dao.addRecipeDirectly(recipe1);
        dao.addRecipeDirectly(recipe2);
        dao.addRecipeDirectly(recipe3);

        FakeSortCookingListPresenter sortPresenter = new FakeSortCookingListPresenter();
        SortCookingListInteractor sortInteractor = new SortCookingListInteractor(dao, sortPresenter);

        SortCookingListInputData input = new SortCookingListInputData(
                "testuser",
                SortCookingListInputData.SortType.BY_HEALTH_SCORE
        );

        // Act
        sortInteractor.execute(input);

        // Assert
        SortCookingListOutputData out = sortPresenter.getLastOutput();
        assertNotNull(out);
        assertEquals("Sorted by health score (highest first)", out.getMessage());

        List<Recipe> sortedList = out.getSortedCookingList();
        assertEquals(3, sortedList.size());
        assertEquals(95, sortedList.get(0).getHealthScore()); // Salad
        assertEquals(85, sortedList.get(1).getHealthScore()); // Smoothie
        assertEquals(60, sortedList.get(2).getHealthScore()); // Pizza
    }

    @Test
    public void executeSortByCaloriesSuccess() {
        // Arrange: Create recipes with different calories
        Recipe recipe1 = Recipe.builder()
                .setId(1)
                .setTitle("Salad")
                .setHealthScore(95)
                .setCalories(200)
                .build();

        Recipe recipe2 = Recipe.builder()
                .setId(2)
                .setTitle("Pizza")
                .setHealthScore(60)
                .setCalories(800)
                .build();

        Recipe recipe3 = Recipe.builder()
                .setId(3)
                .setTitle("Smoothie")
                .setHealthScore(85)
                .setCalories(150)
                .build();

        dao.addRecipeDirectly(recipe1);
        dao.addRecipeDirectly(recipe2);
        dao.addRecipeDirectly(recipe3);

        FakeSortCookingListPresenter sortPresenter = new FakeSortCookingListPresenter();
        SortCookingListInteractor sortInteractor = new SortCookingListInteractor(dao, sortPresenter);

        SortCookingListInputData input = new SortCookingListInputData(
                "testuser",
                SortCookingListInputData.SortType.BY_CALORIES
        );

        // Act
        sortInteractor.execute(input);

        // Assert
        SortCookingListOutputData out = sortPresenter.getLastOutput();
        assertNotNull(out);
        assertEquals("Sorted by calories (lowest first)", out.getMessage());

        List<Recipe> sortedList = out.getSortedCookingList();
        assertEquals(3, sortedList.size());
        assertEquals(150.0, sortedList.get(0).getCalories(), 0.01); // Smoothie
        assertEquals(200.0, sortedList.get(1).getCalories(), 0.01); // Salad
        assertEquals(800.0, sortedList.get(2).getCalories(), 0.01); // Pizza
    }

    @Test
    public void executeSortEmptyListReturnsEmptyList() {
        // Arrange: Empty list
        FakeSortCookingListPresenter sortPresenter = new FakeSortCookingListPresenter();
        SortCookingListInteractor sortInteractor = new SortCookingListInteractor(dao, sortPresenter);

        SortCookingListInputData input = new SortCookingListInputData(
                "newuser",
                SortCookingListInputData.SortType.BY_HEALTH_SCORE
        );

        // Act
        sortInteractor.execute(input);

        // Assert
        SortCookingListOutputData out = sortPresenter.getLastOutput();
        assertNotNull(out);
        assertTrue(out.getSortedCookingList().isEmpty());
        assertEquals("Sorted by health score (highest first)", out.getMessage());
    }

    @Test
    public void executeSortHandlesExceptionFromDao() {
        // Arrange: Configure DAO to throw exception
        dao.setShouldThrowException(true);

        FakeSortCookingListPresenter sortPresenter = new FakeSortCookingListPresenter();
        SortCookingListInteractor sortInteractor = new SortCookingListInteractor(dao, sortPresenter);

        SortCookingListInputData input = new SortCookingListInputData(
                "newuser",
                SortCookingListInputData.SortType.BY_HEALTH_SCORE
        );

        // Act
        sortInteractor.execute(input);

        // Assert: Should return empty list when exception occurs
        SortCookingListOutputData out = sortPresenter.getLastOutput();
        assertNotNull(out);
        assertTrue(out.getSortedCookingList().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructSortInputData_nullUsername_throwsException() {
        new SortCookingListInputData(null, SortCookingListInputData.SortType.BY_HEALTH_SCORE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructSortInputData_emptyUsername_throwsException() {
        new SortCookingListInputData("", SortCookingListInputData.SortType.BY_CALORIES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructSortInputData_nullSortType_throwsException() {
        new SortCookingListInputData("testuser", null);
    }
}