package interface_adapter.fridgemodify;

import entity.Ingredient;
import interface_adapter.login.LoginViewModel;
import use_case.fridge.AddToFridge.AddToFridgeInputBoundary;
import use_case.fridge.AddToFridge.AddToFridgeRequestModel;
import use_case.fridge.DeleteFridge.DeleteFridgeInputBoundary;
import use_case.fridge.DeleteFridge.DeleteFridgeRequestModel;
import use_case.fridge.GetFridge.GetFridgeInputBoundary;
import use_case.fridge.GetFridge.GetFridgeRequestModel;
import use_case.fridge.GetFridge.GetFridgeResponseModel;

import java.util.List;

public class FridgeController {

    private final AddToFridgeInputBoundary addUc;
    private final GetFridgeInputBoundary getUc;
    private final DeleteFridgeInputBoundary deleteUc;
    private final LoginViewModel loginVm;
    private final FridgeViewModel viewModel;

    public FridgeController(
            AddToFridgeInputBoundary addUc,
            GetFridgeInputBoundary getUc,
            DeleteFridgeInputBoundary deleteUc,
            LoginViewModel loginVm,
            FridgeViewModel viewModel
    ) {
        this.addUc = addUc;
        this.getUc = getUc;
        this.deleteUc = deleteUc;
        this.loginVm = loginVm;
        this.viewModel = viewModel;
    }

    /** 添加 ingredient 并刷新 fridge */
    public void addIngredient(String ingredientName) throws Exception {
        final String username = loginVm.getState().getUsername();
        addUc.addIngredient(new AddToFridgeRequestModel(username, ingredientName));
        GetIngredient();
    }

    /** 获取 fridge 列表，并更新 ViewModel */
    public void GetIngredient() throws Exception {
        final String username = loginVm.getState().getUsername();
        GetFridgeResponseModel response = getUc.getIngredient(new GetFridgeRequestModel(username));
        viewModel.setState(responseToFridgeState(response));
    }

    /** 删除 ingredient 并刷新 fridge */
    public void deleteIngredient(Ingredient ingredient) throws Exception {
        deleteUc.deleteIngredient(new DeleteFridgeRequestModel(
                loginVm.getState().getUsername(),
                ingredient.getIngredientId()
        ));
        GetIngredient();
    }

    /** 将 ResponseModel 转换为 FridgeState */
    private FridgeState responseToFridgeState(GetFridgeResponseModel response) {
        FridgeState state = new FridgeState();
        List<Ingredient> ingredients = response.getIngredients();  // 假设 ResponseModel 有 getIngredients()
        state.setIngredients(ingredients);
        return state;
    }
}
