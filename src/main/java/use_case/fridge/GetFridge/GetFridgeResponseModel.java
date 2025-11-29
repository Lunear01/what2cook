package use_case.fridge.GetFridge;

import entity.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class GetFridgeResponseModel {

    private final String userName;
    private final List<Ingredient> ingredients;


    public GetFridgeResponseModel(String userName,  List<Ingredient> ingredients) {
            this.userName = userName;
            this.ingredients = ingredients;
        }

        public String getUserName() {
            return userName;
        }

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

}
