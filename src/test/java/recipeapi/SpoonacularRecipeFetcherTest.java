package recipeapi;
import entity.Recipe;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class SpoonacularRecipeFetcherTest {
    @Test
    public void testRealAPI() {
        RecipeFetcher fetcher = new SpoonacularRecipeFetcher();

        /* === Testing getRecipesByIngredients === */
        try {
            List<Recipe> recipes = fetcher.getRecipesByIngredients(
                    List.of("chicken", "rice"),
                    3,
                    1,
                    true
            );

            // Print recipe names
            for (Recipe r : recipes) {
                System.out.println("Recipe: " + r.getTitle() + " (ID: " + r.getId() + ")");
            }

            // Basic assertions to ensure it's not empty
            assertFalse(recipes.isEmpty());

        } catch (RecipeFetcher.IngredientNotFoundException e) {
            fail("Ingredient not found: " + e.getMessage());
        }

        /* === Testing getRecipeInfo === */
        try {
            int id = 715538; // Known working recipe ID

            Recipe info = fetcher.getRecipeInfo(id, true, false, false);

            System.out.println("Title: " + info.getTitle());
            System.out.println("Calories: " + info.getCalories());
            System.out.println("Health Score: " + info.getHealthScore());

            assertNotNull(info.getTitle());

        } catch (RecipeFetcher.RecipeNotFoundException e) {
            fail("Recipe not found: " + e.getMessage());
        }

        /* === Testing getRecipeInstructions === */
        try {
            int id = 715538;

            Recipe instructions = fetcher.getRecipeInstructions(id, true);

            System.out.println(instructions.getInstructions());

            assertNotNull(instructions.getInstructions());

        } catch (RecipeFetcher.RecipeNotFoundException e) {
            fail("Recipe not found: " + e.getMessage());
        }

        /* === Testing getNutrition === */
        try {
            int id = 715538;

            Recipe nutrition = fetcher.getNutrition(id);

            System.out.println("Calories: " + nutrition.getCalories());

            assertTrue(nutrition.getCalories() > 0);

        } catch (RecipeFetcher.RecipeNotFoundException e) {
            fail("Recipe not found: " + e.getMessage());
        }
    }
}
