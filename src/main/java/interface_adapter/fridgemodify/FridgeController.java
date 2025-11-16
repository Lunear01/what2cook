package interface_adapter.fridgemodify;

import use_case.AddIngredientUseCase;
import use_case.GetIngredientUseCase;

public class FridgeController {

    private final AddIngredientUseCase addin;
    private final GetIngredientUseCase getin;

    private final FridgeViewModel vm;

    public FridgeController(AddIngredientUseCase addin,
                            FridgeViewModel VM) {
        this.addin = addin;
        this.vm = VM;
    }

    public void addIngredient(String name) {
        addin.addToList(name);
    }

    public void refresh() {
        vm.update(getin.execute());
    }
}

