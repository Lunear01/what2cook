package recipeapi;
import entity.Recipe;
import org.junit.Test;
import recipeapi.exceptions.IngredientNotFoundException;
import recipeapi.exceptions.RecipeNotFoundException;

import java.io.IOException;
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

            // Print recipe names and images
            for (Recipe r : recipes) {
                System.out.println("Recipe: " + r.getTitle() + " (ID: " + r.getId() + ")");
                System.out.println("Image: " + r.getImage());
            }

            // Basic assertions to ensure it's not empty
            assertFalse(recipes.isEmpty());

        }
        catch (IngredientNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* === Testing getRecipeInfo === */
        try {
            int id = 649985; // Known working recipe ID

            Recipe info = fetcher.getRecipeInfo(id, true, false, false);

            System.out.println("Title: " + info.getTitle());
            System.out.println("Health Score: " + info.getHealthScore());
            System.out.println("Calories: " + info.getCalories());

            assertNotNull(info.getTitle());

        } catch (RecipeNotFoundException | IOException e) {
            fail(e.getMessage());
        }

        /* === Testing getRecipeInstructions === */
        try {
            int id = 649985;

            Recipe instructions = fetcher.getRecipeInstructions(id, true);

            System.out.println(instructions.getInstructions());

            assertNotNull(instructions.getInstructions());

        } catch (RecipeNotFoundException | IOException e) {
            fail(e.getMessage());
        }
    }
}
