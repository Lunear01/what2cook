package use_case.recipe_search;

import java.util.ArrayList;
import java.util.List;

import entity.Ingredient;
import entity.Recipe;
import recipeapi.RecipeFetcher;

public class RecipeSearchInteractor implements RecipeSearchInputBoundary {

    private static final int DEFAULT_NUMBER = 10;
    private static final int DEFAULT_RANKING = 1;
    private static final boolean DEFAULT_IGNORE_PANTRY = true;

    private final RecipeFetcher fetcher;
    private final RecipeSearchOutputBoundary presenter;

    public RecipeSearchInteractor(RecipeFetcher fetcher,
                                  RecipeSearchOutputBoundary presenter) {
        this.fetcher = fetcher;
        this.presenter = presenter;
    }

    @Override
    public void execute(RecipeSearchInputData inputData) {
        final List<Ingredient> ingredients = inputData.getIngredients();

        if (ingredients == null || ingredients.isEmpty()) {
            presenter.prepareFailView(new RecipeSearchOutputData(
                    new ArrayList<>(),
                    new ArrayList<>(),
                    "Please add at least one ingredient before searching."
            ));
            return;
        }

        try {
            final List<String> names = new ArrayList<>();
            for (Ingredient ing : ingredients) names.add(ing.getName());

            final List<Recipe> basic = fetcher.getRecipesByIngredients(
                    names, DEFAULT_NUMBER, DEFAULT_RANKING, DEFAULT_IGNORE_PANTRY);

            final List<Recipe> enriched = new ArrayList<>();






            for (Recipe r : basic) {
                final int id = r.getId();
                final String title = r.getTitle();
                final String image = r.getImage();

                final Recipe info = fetcher.getRecipeInfo(id, true, false, false);

                // 用 toBuilder() 在原本的 r 上更新 fields
                Recipe updated = r.toBuilder()
                        .setHealthScore(info.getHealthScore())
                        .setIngredientNames(info.getIngredientNames())
                        .setCalories(info.getCalories())
                        .setInstructions(instructions.getInstructions())
                        .build();

                enriched.add(updated);
            }
          
            for (Recipe r : basic) {
                final int id = r.getId();

                // 取得 recipe 的 info
                final Recipe info = fetcher.getRecipeInfo(id, true, false, false);

                // 用 toBuilder() 在原本的 r 上更新 fields
                Recipe updated = r.builder()
                        .setHealthScore(info.getHealthScore())
                        .setIngredientNames(info.getIngredientNames())
                        .setCalories(info.getCalories())
                        .build();

                // 再取得 instructions
                final Recipe instructions = fetcher.getRecipeInstructions(id, true);

                // 再把 instructions 加进去（再次 toBuilder）
                updated = updated.toBuilder()
                        .setInstructions(instructions.getInstructions())
                        .build();

                enriched.add(updated);
            }

            presenter.prepareSuccessView(new RecipeSearchOutputData(
                    new ArrayList<>(ingredients),
                    enriched,
                    null
            ));


        }
        catch (Exception e) {
            presenter.prepareFailView(new RecipeSearchOutputData(
                    new ArrayList<>(ingredients),
                    new ArrayList<>(),
                    "Failed to fetch recipes: " + e.getMessage()
            ));
        }
    }
}
