package interface_adapter.ingredient_search;

import use_case.ingredient_search.IngredientSearchOutputBoundary;
import use_case.ingredient_search.IngredientSearchOutputData;

public class IngredientSearchPresenter implements IngredientSearchOutputBoundary {

    private final IngredientSearchViewModel viewModel;

    public IngredientSearchPresenter(IngredientSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(IngredientSearchOutputData data) {
        IngredientSearchState state = new IngredientSearchState();
        state.setIngredients(data.getIngredientList());
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}

