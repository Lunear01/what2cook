package interface_adapter.recipe_search;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import entity.Ingredient;
import entity.Recipe;
import recipeapi.RecipeFetcher;
import recipeapi.SpoonacularRecipeFetcher;

public class RecipeSearchController {

    private static final int DEFAULT_NUMBER = 10;
    private static final int DEFAULT_RANKING = 1;
    private static final boolean DEFAULT_IGNORE_PANTRY = true;

    private final RecipeSearchViewModel viewModel;
    private final RecipeFetcher fetcher;

    public RecipeSearchController(RecipeSearchViewModel viewModel) {
        this(viewModel, new SpoonacularRecipeFetcher());
    }

    public RecipeSearchController(RecipeSearchViewModel viewModel, RecipeFetcher fetcher) {
        this.viewModel = viewModel;
        this.fetcher = fetcher;
    }

    /**
     * Search recipes using API based on the given ingredients.
     *
     * @param ingredients list of ingredients selected by the user.
     */
    public void searchByIngredients(List<Ingredient> ingredients) {
        final RecipeSearchState newState = new RecipeSearchState(viewModel.getState());

        if (ingredients == null || ingredients.isEmpty()) {
            newState.setIngredients(new ArrayList<>());
            newState.setRecipes(new ArrayList<>());
            newState.setError("Please add at least one ingredient before searching.");
            viewModel.setState(newState);
            viewModel.firePropertyChanged();
            return;
        }

        try {
            final List<String> names = new ArrayList<>();
            for (Ingredient ing : ingredients) {
                names.add(ing.getName());
            }

            final List<Recipe> basic =
                    fetcher.getRecipesByIngredients(
                            names,
                            DEFAULT_NUMBER,
                            DEFAULT_RANKING,
                            DEFAULT_IGNORE_PANTRY
                    );

            final List<Recipe> enriched = new ArrayList<>();
            for (Recipe r : basic) {
                final int id = r.getId();

                final Recipe info = fetcher.getRecipeInfo(id, true, false, false);
                r.setHealthScore(info.getHealthScore());
                r.setIngredientNames(info.getIngredientNames());
                r.setCalories(info.getCalories());

                final Recipe instructions = fetcher.getRecipeInstructions(id, true);
                r.setInstructions(instructions.getInstructions());

                enriched.add(r);
            }

            newState.setIngredients(new ArrayList<>(ingredients));
            newState.setRecipes(enriched);
            newState.setError(null);

        }
        catch (Exception exe) {
            final List<Ingredient> safeIngredients;
            if (ingredients == null) {
                safeIngredients = new ArrayList<>();
            }
            else {
                safeIngredients = new ArrayList<>(ingredients);
            }
            newState.setIngredients(safeIngredients);
            newState.setRecipes(new ArrayList<>());
            newState.setError("Failed to fetch recipes: " + exe.getMessage());
        }

        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    /**
     * Opens a dialog showing detailed recipe information.
     *
     * @param recipe the selected recipe.
     */
    public void openRecipe(Recipe recipe) {
        if (recipe == null) {
            return;
        }

        ImageIcon icon = null;
        if (recipe.getImage() != null) {
            icon = loadImage(recipe.getImage());
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(recipe.getTitle()).append("\n");
        sb.append("Calories: ").append(recipe.getCalories()).append("\n");
        sb.append("Health Score: ").append(recipe.getHealthScore()).append("\n");

        sb.append("\nIngredients:\n");
        for (Ingredient ing : recipe.getIngredientNames()) {
            sb.append("  - ").append(ing.getName()).append("\n");
        }

        sb.append("\nInstructions:\n");
        sb.append(recipe.getInstructions());

        JOptionPane.showMessageDialog(
                null,
                sb.toString(),
                recipe.getTitle(),
                JOptionPane.INFORMATION_MESSAGE,
                icon
        );
    }
    // Helper method to get and scale recipe image
    private ImageIcon loadImage(String urlStr) {
        try {
            java.net.URL url = new java.net.URL(urlStr);
            java.awt.Image img = javax.imageio.ImageIO.read(url);

            // Scaling if needed
            img = img.getScaledInstance(300, -1, java.awt.Image.SCALE_SMOOTH);

            return new ImageIcon(img);
        }
        catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            return null;
        }
    }
}
