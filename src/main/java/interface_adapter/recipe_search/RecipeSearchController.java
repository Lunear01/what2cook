package interface_adapter.recipe_search;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import entity.Ingredient;
import entity.Recipe;
import recipeapi.RecipeFetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeSearchController {

    private final RecipeSearchViewModel viewModel;
    private final RecipeFetcher fetcher;

    // ✅ 构造：用于搜索 + 更新VM
    public RecipeSearchController(RecipeSearchViewModel viewModel, RecipeFetcher fetcher) {
        this.viewModel = viewModel;
        this.fetcher = fetcher;
    }

    /**
     * 通过输入的食材字符串搜索菜品。
     * input 示例: "apple, flour, milk"
     */
    public void searchByIngredients(String input) {
        if (input == null) input = "";
        input = input.trim();

        // 1) 解析输入 => ingredients list
        List<String> ingredients = new ArrayList<>();
        if (!input.isEmpty()) {
            ingredients = Arrays.stream(input.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

        }

        try {
            // 2) 调 Spoonacular：最多 10 个结果，ranking=1, ignorePantry=true
            List<Recipe> results = fetcher.getRecipesByIngredients(
                    ingredients, 10, 1, true
            );

            // 3) 写回 ViewModel
            RecipeSearchState state = new RecipeSearchState(viewModel.getState());
            state.setIngredients(ingredients);
            state.setRecipes(results);
            state.setError(null);

            viewModel.setState(state);
            viewModel.firePropertyChanged();

        } catch (RecipeFetcher.IngredientNotFoundException e) {
            RecipeSearchState state = new RecipeSearchState(viewModel.getState());
            state.setIngredients(ingredients);
            state.setRecipes(new ArrayList<>());
            state.setError(e.getMessage());

            viewModel.setState(state);
            viewModel.firePropertyChanged();

        } catch (Exception e) {
            RecipeSearchState state = new RecipeSearchState(viewModel.getState());
            state.setIngredients(ingredients);
            state.setRecipes(new ArrayList<>());
            state.setError("Search failed: " + e.getMessage());

            viewModel.setState(state);
            viewModel.firePropertyChanged();
        }
    }

    /**
     * 点击 recipe card 弹详情。
     * findByIngredients 返回的 recipe 不完整，
     * 这里用 id 再补全 info / nutrition / instructions。
     */
    public void openRecipe(Recipe recipe) {
        if (recipe == null) return;

        try {
            int id = recipe.getId();

            // ---- 补全 info（healthScore, ingredients详细等）----
            Recipe info = fetcher.getRecipeInfo(id, false, false, false);
            if (info.getHealthScore() != 0) {
                recipe.setHealthScore(info.getHealthScore());
            }
            if (info.getIngredientNames() != null && !info.getIngredientNames().isEmpty()) {
                recipe.setIngredientNames(info.getIngredientNames());
            }

            // ---- 补全 nutrition（calories）----
            Recipe nutri = fetcher.getNutrition(id);
            if (nutri.getCalories() != 0) {
                // 注意：setCalories(int) 需要强转
                recipe.setCalories((int) nutri.getCalories());
            }

            // ---- 补全 instructions ----
            Recipe instr = fetcher.getRecipeInstructions(id, true);
            if (instr.getInstructions() != null && !instr.getInstructions().isEmpty()) {
                recipe.setInstructions(instr.getInstructions());
            }

        } catch (Exception e) {
            // 拉不到也没关系，继续用已有字段弹窗
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(recipe.getTitle()).append("\n");
        sb.append("Calories: ").append(recipe.getCalories()).append("\n");
        sb.append("Health Score: ").append(recipe.getHealthScore()).append("\n");
        sb.append("Ingredients:\n");

        if (recipe.getIngredientNames() != null) {
            for (Ingredient ing : recipe.getIngredientNames()) {
                sb.append("  - ").append(ing.getName()).append("\n");
            }
        }

        sb.append("\nInstructions:\n")
                .append(recipe.getInstructions() == null ? "No instructions." : recipe.getInstructions());

        JOptionPane.showMessageDialog(null, sb.toString(),
                recipe.getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }
}
