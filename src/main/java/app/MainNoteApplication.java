package app;

import entity.Recipe;
import interface_adapter.recipe_search.RecipeSearchController;
import interface_adapter.recipe_search.RecipeSearchState;
import interface_adapter.recipe_search.RecipeSearchViewModel;
import view.RecipeSearchView;

import javax.swing.*;
import java.util.Arrays;

public class MainNoteApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // 1. 创建 ViewModel
            RecipeSearchViewModel viewModel = new RecipeSearchViewModel();

            // 2. 创建 View
            RecipeSearchView recipeSearchView = new RecipeSearchView(viewModel);

            // 3. 绑定一个简单的 Controller（只是弹出对话框）
            RecipeSearchController controller = new RecipeSearchController();
            recipeSearchView.setController(controller);

            // 4. 构造几条 dummy Recipe 数据（完全不依赖真实 ingredients）
            Recipe r1 = new Recipe(
                    1,                                      // id
                    "Pasta Carbonara",                      // title
                    Arrays.asList("Pasta", "Egg", "Bacon"), // ingredientNames
                    520.0,                                  // calories
                    70,                                     // healthScore
                    "Boil pasta. Mix eggs and bacon. Combine and serve."
            );
            // 如果你有图片，可以设路径；没有也没关系，会显示空白
            r1.setImage("images/pasta.jpg");

            Recipe r2 = new Recipe(
                    2,
                    "Tomato Soup",
                    Arrays.asList("Tomato", "Salt", "Water"),
                    150.0,
                    90,
                    "Cook tomatoes. Blend. Add water. Cook again."
            );
            r2.setImage("images/soup.jpg");

            // 5. 把 dummy 数据塞进 State，然后推给 ViewModel
            RecipeSearchState state = new RecipeSearchState();
            // 这里 ingredients 随便写点占位就好，UI 里只是显示一行文字
            state.setIngredients(Arrays.asList("dummy egg", "dummy milk"));
            state.setRecipes(Arrays.asList(r1, r2));

            viewModel.setState(state);

            // 6. 创建 JFrame，把 View 放进去
            JFrame frame = new JFrame("What2Cook");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(recipeSearchView);
            frame.pack();
            frame.setLocationRelativeTo(null); // 居中
            frame.setVisible(true);
        });
    }
}
