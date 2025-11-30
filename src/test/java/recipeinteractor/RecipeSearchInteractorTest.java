package use_case.recipe_search;

import entity.Ingredient;
import entity.Recipe;
import org.junit.Before;
import org.junit.Test;
import recipeapi.RecipeFetcher;
import recipeapi.exceptions.IngredientNotFoundException;
import recipeapi.exceptions.RecipeNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

// Test double for presenter: captures last output data
class FakeRecipeSearchPresenter implements RecipeSearchOutputBoundary {

    RecipeSearchOutputData lastOutput;

    @Override
    public void prepareSuccessView(RecipeSearchOutputData data) {
        this.lastOutput = data;
    }

    @Override
    public void prepareFailView(RecipeSearchOutputData data) {
        this.lastOutput = data;
    }
}

// Configurable fake fetcher to trigger different paths
class FakeRecipeFetcher implements RecipeFetcher {

    enum Mode {
        SUCCESS,
        INGREDIENT_NOT_FOUND,
        RECIPE_NOT_FOUND_ON_INFO,
        RECIPE_NOT_FOUND_ON_INSTR,
        IO_EXCEPTION
    }

    Mode mode = Mode.SUCCESS;

    int byIngredientsCalls;
    int infoCalls;
    int instructionsCalls;

    @Override
    public List<Recipe> getRecipesByIngredients(List<String> ingredients,
                                                int number,
                                                int ranking,
                                                boolean ignorePantry)
            throws IngredientNotFoundException, IOException {

        byIngredientsCalls++;

        switch (mode) {
            case INGREDIENT_NOT_FOUND:
                throw new IngredientNotFoundException("No recipes for: " + String.join(",", ingredients));
            case IO_EXCEPTION:
                throw new IOException("network down");
            default:
                // SUCCESS or recipe not found cases: return one basic recipe
                Ingredient ing = Ingredient.builder()
                        .setName(ingredients.get(0))
                        .setId(1)
                        .build();

                Recipe basic = Recipe.builder()
                        .setId(100)
                        .setTitle("Basic")
                        .setImage("img.png")
                        .setIngredientNames(Collections.singletonList(ing))
                        .build();

                List<Recipe> list = new ArrayList<>();
                list.add(basic);
                return list;
        }
    }

    @Override
    public Recipe getRecipeInfo(int id,
                                boolean includeNutrition,
                                boolean addWinePairing,
                                boolean addTasteData)
            throws RecipeNotFoundException, IOException {

        infoCalls++;

        if (mode == Mode.RECIPE_NOT_FOUND_ON_INFO) {
            throw new RecipeNotFoundException("info missing for " + id);
        }
        if (mode == Mode.IO_EXCEPTION) {
            throw new IOException("network down on info");
        }

        return Recipe.builder()
                .setId(id)
                .setTitle("Info " + id)
                .setHealthScore(80)
                .setCalories(300)
                .setIngredientNames(Collections.singletonList(
                        Ingredient.builder().setName("infoIng").setId(2).build()
                ))
                .build();
    }

    @Override
    public Recipe getRecipeInstructions(int id, boolean stepBreakdown)
            throws RecipeNotFoundException, IOException {

        instructionsCalls++;

        if (mode == Mode.RECIPE_NOT_FOUND_ON_INSTR) {
            throw new RecipeNotFoundException("instructions missing for " + id);
        }
        if (mode == Mode.IO_EXCEPTION) {
            throw new IOException("network down on instr");
        }

        return Recipe.builder()
                .setId(id)
                .setInstructions("1. Do something\n2. Do more")
                .build();
    }
}

public class RecipeSearchInteractorTest {

    private FakeRecipeFetcher fetcher;
    private FakeRecipeSearchPresenter presenter;
    private RecipeSearchInteractor interactor;

    @Before
    public void setUp() {
        fetcher = new FakeRecipeFetcher();
        presenter = new FakeRecipeSearchPresenter();
        interactor = new RecipeSearchInteractor(fetcher, presenter);
    }

    @Test
    public void execute_noIngredients_nullList_triggersFailView() {
        RecipeSearchInputData input = new RecipeSearchInputData(null);

        interactor.execute(input);

        assertNotNull(presenter.lastOutput);
        assertTrue(presenter.lastOutput.getIngredients().isEmpty());
        assertTrue(presenter.lastOutput.getRecipes().isEmpty());
        assertEquals("Please add at least one ingredient before searching.",
                presenter.lastOutput.getErrorMessage());
    }

    @Test
    public void execute_noIngredients_emptyList_triggersFailView() {
        RecipeSearchInputData input = new RecipeSearchInputData(new ArrayList<>());

        interactor.execute(input);

        assertNotNull(presenter.lastOutput);
        assertTrue(presenter.lastOutput.getIngredients().isEmpty());
        assertTrue(presenter.lastOutput.getRecipes().isEmpty());
        assertEquals("Please add at least one ingredient before searching.",
                presenter.lastOutput.getErrorMessage());
    }

    @Test
    public void execute_success_enrichesRecipes_andCallsSuccessView() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.builder().setName("milk").setId(1).build());
        RecipeSearchInputData input = new RecipeSearchInputData(ingredients);

        fetcher.mode = FakeRecipeFetcher.Mode.SUCCESS;

        interactor.execute(input);

        RecipeSearchOutputData out = presenter.lastOutput;
        assertNotNull(out);
        assertNull(out.getErrorMessage());
        assertEquals(1, out.getIngredients().size());
        assertEquals("milk", out.getIngredients().get(0).getName());

        assertEquals(1, out.getRecipes().size());
        Recipe enriched = out.getRecipes().get(0);
        assertEquals(100, enriched.getId());
        assertEquals(80, enriched.getHealthScore());
        assertNotNull(enriched.getInstructions());
    }

    @Test
    public void execute_ingredientNotFound_triggersFailViewWithMessage() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.builder().setName("unicorn dust").setId(1).build());
        RecipeSearchInputData input = new RecipeSearchInputData(ingredients);

        fetcher.mode = FakeRecipeFetcher.Mode.INGREDIENT_NOT_FOUND;

        interactor.execute(input);

        RecipeSearchOutputData out = presenter.lastOutput;
        assertNotNull(out);
        assertEquals(1, out.getIngredients().size());
        assertTrue(out.getRecipes().isEmpty());
        assertTrue(out.getErrorMessage().contains("No recipes for"));
    }

    @Test
    public void execute_recipeNotFound_onInfo_triggersFailViewWithMessage() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.builder().setName("milk").setId(1).build());
        RecipeSearchInputData input = new RecipeSearchInputData(ingredients);

        fetcher.mode = FakeRecipeFetcher.Mode.RECIPE_NOT_FOUND_ON_INFO;

        interactor.execute(input);

        RecipeSearchOutputData out = presenter.lastOutput;
        assertNotNull(out);
        assertEquals(1, out.getIngredients().size());
        assertTrue(out.getRecipes().isEmpty());
        assertTrue(out.getErrorMessage().contains("info missing"));
    }

    @Test
    public void execute_recipeNotFound_onInstructions_triggersFailViewWithMessage() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.builder().setName("milk").setId(1).build());
        RecipeSearchInputData input = new RecipeSearchInputData(ingredients);

        fetcher.mode = FakeRecipeFetcher.Mode.RECIPE_NOT_FOUND_ON_INSTR;

        interactor.execute(input);

        RecipeSearchOutputData out = presenter.lastOutput;
        assertNotNull(out);
        assertEquals(1, out.getIngredients().size());
        assertTrue(out.getRecipes().isEmpty());
        assertTrue(out.getErrorMessage().contains("instructions missing"));
    }

    @Test
    public void execute_ioException_triggersFailViewWithNetworkMessage() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(Ingredient.builder().setName("milk").setId(1).build());
        RecipeSearchInputData input = new RecipeSearchInputData(ingredients);

        fetcher.mode = FakeRecipeFetcher.Mode.IO_EXCEPTION;

        interactor.execute(input);

        RecipeSearchOutputData out = presenter.lastOutput;
        assertNotNull(out);
        assertEquals(1, out.getIngredients().size());
        assertTrue(out.getRecipes().isEmpty());
        assertTrue(out.getErrorMessage().startsWith("Network error while fetching recipes: "));
    }
}
