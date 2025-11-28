package use_case.recipe_search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Input data for the recipe search use case.
 * For now it only carries ingredient names as Strings.
 */
public class RecipeSearchInputData {

    private final List<String> ingredientNames;

    /**
     * Construct input data from a list of ingredient names.
     *
     * @param ingredientNames list of user-provided ingredient names
     */
    public RecipeSearchInputData(List<String> ingredientNames) {
        if (ingredientNames == null) {
            this.ingredientNames = Collections.emptyList();
        } else {
            this.ingredientNames = Collections.unmodifiableList(
                    new ArrayList<>(ingredientNames));
        }
    }

    /**
     * @return unmodifiable list of ingredient names
     */
    public List<String> getIngredientNames() {
        return ingredientNames;
    }
}
