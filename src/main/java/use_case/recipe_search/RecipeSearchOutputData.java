package use_case.recipe_search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entity.Recipe;

/**
 * Output data for the recipe search use case.
 */
public class RecipeSearchOutputData {

    private final List<Recipe> recipes;
    private final boolean useCaseFailed;
    private final String errorMessage;

    /**
     * @param recipes       list of recipes returned by the API (may be empty)
     * @param useCaseFailed true if the use case failed (e.g. no recipes / network error)
     * @param errorMessage  human-readable message for the view (nullable)
     */
    public RecipeSearchOutputData(List<Recipe> recipes,
                                  boolean useCaseFailed,
                                  String errorMessage) {
        if (recipes == null) {
            this.recipes = Collections.emptyList();
        } else {
            this.recipes = Collections.unmodifiableList(
                    new ArrayList<>(recipes));
        }
        this.useCaseFailed = useCaseFailed;
        this.errorMessage = errorMessage;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
