package use_case.ingredient_search;

import java.util.ArrayList;
import java.util.List;

import dataaccess.IngredientDataAccess;
import entity.Ingredient;

/**
 * Use case for adding / listing ingredients for a specific user.
 */
public class IngredientSearchInteractor implements IngredientSearchInputBoundary {

    private final IngredientDataAccess ingredientDAO;
    private final IngredientSearchOutputBoundary presenter;

    public IngredientSearchInteractor(IngredientDataAccess ingredientDAO,
                                      IngredientSearchOutputBoundary presenter) {
        this.ingredientDAO = ingredientDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(IngredientSearchInputData data) {

        final String userName = data.getUserName();
        String input = data.getIngredientName();
        if (input == null) {
            input = "";
        }
        input = input.trim();

        try {
            // 如果这次调用传了新的食材，就先存到后端
            if (!input.isEmpty()) {
                // 暂时 ingredientId 用 -1 占位，真正 id 由后端生成也无所谓
                final int placeholderId = -1;
                ingredientDAO.addIngredient(userName, placeholderId, input);
            }

            // 再从后端把这个用户的所有食材读回来
            final List<Ingredient> ingEntities = ingredientDAO.getAllIngredients(userName);

            // 转成名字列表传给 presenter（你的 state 里是 List<String>）
            final List<String> names = new ArrayList<>();
            for (Ingredient ing : ingEntities) {
                names.add(ing.getName());
            }

            presenter.present(new IngredientSearchOutputData(names));
        }
        catch (Exception e) {
            // 发生异常时，先给个空列表，之后你可以在 OutputData 里再加 error 字段
            presenter.present(new IngredientSearchOutputData(new ArrayList<>()));
        }
    }
}
