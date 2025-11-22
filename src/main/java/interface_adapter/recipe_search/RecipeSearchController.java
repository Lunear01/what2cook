package interface_adapter.recipe_search;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import entity.Ingredient;
import entity.Recipe;

public class RecipeSearchController {

    private final RecipeSearchViewModel viewModel;

    public RecipeSearchController(RecipeSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Search recipes using the given list of ingredients.
     * For now this builds a single demo Recipe so that the UI can be tested.
     */
    public void searchByIngredients(List<Ingredient> ingredients) {

        final List<Recipe> recipes = new ArrayList<>();
        String error = null;

        if (ingredients != null && !ingredients.isEmpty()) {

            // Build a demo title like "Recipe with apples, milk"
            final StringBuilder titleBuilder = new StringBuilder("Recipe with ");
            for (int i = 0; i < ingredients.size(); i++) {
                if (i > 0) titleBuilder.append(", ");
                titleBuilder.append(ingredients.get(i).getName());
            }

            // Demo recipe; replace this with real API / use-case later
            final Recipe demo = new Recipe(
                    1,
                    titleBuilder.toString(),
                    ingredients,
                    300.0,
                    80,
                    "This is a demo recipe using your ingredients."
            );
            demo.setImage(""); // no image for now

            recipes.add(demo);
        } else {
            // No ingredients provided
            error = "Please add at least one ingredient before searching.";
        }

        // Build a new state so we keep previous values where needed
        final RecipeSearchState newState = new RecipeSearchState(viewModel.getState());
        newState.setIngredients(
                ingredients == null ? new ArrayList<>() : new ArrayList<>(ingredients));
        newState.setRecipes(recipes);
        newState.setError(error);

        // Update ViewModel and notify the view
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    /**
     * Opens a dialog showing the details of a selected recipe.
     */
    public void openRecipe(Recipe recipe) {
        if (recipe == null) return;

        final StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(recipe.getTitle()).append("\n");
        sb.append("Calories: ").append(recipe.getCalories()).append("\n");
        sb.append("Health Score: ").append(recipe.getHealthScore()).append("\n");

        sb.append("Ingredients:\n");
        for (Ingredient ing : recipe.getIngredientNames()) {
            sb.append("  - ").append(ing.getName()).append("\n");
        }

        sb.append("\nInstructions:\n");
        sb.append(recipe.getInstructions());

        JOptionPane.showMessageDialog(
                null,
                sb.toString(),
                recipe.getTitle(),
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
