package view;

import interface_adapter.cookinglist.AddToCookingListController;
import interface_adapter.cookinglist.CookingListViewModel;
import interface_adapter.cookinglist.RemoveFromCookingListController;
import interface_adapter.recipe_search.RecipeSearchController;
import interface_adapter.recipe_search.RecipeSearchViewModel;

import javax.swing.*;
import java.awt.*;

public class AppView extends JFrame {

    public AppView(String username,
                   RecipeSearchViewModel recipeSearchVM,
                   RecipeSearchController recipeSearchController,
                   CookingListViewModel cookingListVM,
                   AddToCookingListController addToCookingListController,
                   RemoveFromCookingListController removeFromCookingListController) {

        super("What2Cook");

        JTabbedPane tabs = new JTabbedPane();

        // Tab 1: Recipe Search
        RecipeSearchView recipeSearchView = new RecipeSearchView(recipeSearchVM);
        recipeSearchView.setController(recipeSearchController);
        recipeSearchView.setAddToCookingListController(addToCookingListController, username);
        tabs.addTab("Recipe Search", recipeSearchView);

        // Tab 2: Cooking List（✅ 传 removeController + username）
        CookingListView cookingListView = new CookingListView(
                cookingListVM,
                recipeSearchController,
                removeFromCookingListController,
                username
        );
        tabs.addTab("Cooking List", cookingListView);

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);

        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
