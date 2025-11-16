package use_case.recipe;

import entity.Recipe;
import entity.Ingredient;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class RecipeInteractorTest {

    @Test
    public void testExecuteSuccess() {

        // Mock RecipeFetcherInterface
        RecipeFetcherInterface mockFetcher = new RecipeFetcherInterface() {
            @Override
            public List<Recipe> getRecipesByIngredients(String ingredients) {
                // Return fake recipes for testing
                Recipe r1 = new Recipe(123, "Test Recipe");
                Recipe r2 = new Recipe(456, "Another Recipe");
                return Arrays.asList(r1, r2);
            }

            @Override
            public Recipe getRecipeInfo(int id) {
                return new Recipe(id, "Recipe Info");
            }

            @Override
            public List<String> getRecipeInstructions(int id) {
                return Arrays.asList("Step 1", "Step 2");
            }

            @Override
            public Nutrition getNutrition(int id) {
                return new Nutrition(500);
            }
        };

        // Mock output boundary
        RecipeOutputBoundary mockOB = new RecipeOutputBoundary() {
            @Override
            public void prepareSuccessView(List<Recipe> recipes) {
                assertEquals(2, recipes.size());
                assertEquals("Test Recipe", recipes.get(0).getTitle());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Should not reach fail view: " + errorMessage);
            }
        };

        // Interactor
        RecipeInteractor interactor = new RecipeInteractor(mockFetcher, mockOB);

        // Run test
        interactor.execute("chicken,tomato");
    }
}
