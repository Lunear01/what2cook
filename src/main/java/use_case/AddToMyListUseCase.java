package use_case;

import java.util.ArrayList;

/**
 * GetAverageGradeUseCase class.
 */
public class AddToMyListUseCase {
    private final ArrayList<String> myList;

    public AddToMyListUseCase(ArrayList<String> myList) {
        this.myList = myList;
    }

    /**
     * Add the new Ingredient to MyList
     * @param ingredient the new ingredient
     */
    public void addMyList(String ingredient) {
        myList.add(ingredient);
    }
}
