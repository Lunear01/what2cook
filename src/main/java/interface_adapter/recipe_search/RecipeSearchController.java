package interface_adapter.recipe_search;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

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
     * Search recipes using Spoonacular API based on the given ingredients.
     *
     * @param ingredients list of ingredients selected by the user.
     */
    public void searchByIngredients(List<Ingredient> ingredients) {
        final RecipeSearchState newState = new RecipeSearchState(viewModel.getState());

        if (ingredients == null || ingredients.isEmpty()) {
            newState.setIngredients(new ArrayList<Ingredient>());
            newState.setRecipes(new ArrayList<Recipe>());
            newState.setError("Please add at least one ingredient before searching.");
            viewModel.setState(newState);
            viewModel.firePropertyChanged();
            return;
        }

        try {
            // 1. 把 Ingredient 转成名字列表传给 API
            final List<String> names = new ArrayList<>();
            for (Ingredient ing : ingredients) {
                names.add(ing.getName());
            }

            // 2. 调用 Spoonacular：按食材搜食谱
            final List<Recipe> basic =
                    fetcher.getRecipesByIngredients(
                            names,
                            DEFAULT_NUMBER,
                            DEFAULT_RANKING,
                            DEFAULT_IGNORE_PANTRY
                    );

            // 3. 对每个食谱补充：热量、健康分数、详细食材和步骤
            final List<Recipe> enriched = new ArrayList<>();
            for (Recipe r : basic) {
                final int id = r.getId();

                // 营养信息：calories
                final Recipe nutrition = fetcher.getNutrition(id);
                // 这里显式从 double 转成 int，修复你现在的编译错误
                r.setCalories((int) nutrition.getCalories());

                // 详细信息：healthScore + ingredientNames
                final Recipe info = fetcher.getRecipeInfo(id, true, false, false);
                r.setHealthScore(info.getHealthScore());
                r.setIngredientNames(info.getIngredientNames());

                // 步骤说明
                final Recipe instructions = fetcher.getRecipeInstructions(id, true);
                r.setInstructions(instructions.getInstructions());

                enriched.add(r);
            }

            newState.setIngredients(new ArrayList<>(ingredients));
            newState.setRecipes(enriched);
            newState.setError(null);

        } catch (Exception exe) {
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
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
