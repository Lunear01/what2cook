const RecipeService = require("../services/RecipeServices");
const mysql = require("../config/db");

exports.addRecipe = async (req, res) => {
    try {
        const { user_name, recipes } = req.body;

        if (!user_name || !recipes) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const [userRows] = await mysql.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );
        if (userRows.length === 0) {
            return res.status(404).json({ error: "User not found" });
        }

        const user_id = userRows[0].user_id;

        const result = await RecipeService.addRecipe(user_id, recipes);

        res.json({
            success: true,
            message: "recipe added",
            recipeId: result.insertId
        });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Failed to add recipe" });
    }
};


exports.getRecipe = async (req, res) => {
    try {
        const { user_name } = req.params;

        const recipeList = await RecipeService.getRecipeByUser(user_name);

        res.json({ success: true, recipe: recipeList });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Failed to get recipe" });
    }
};


exports.deleteRecipe = async (req, res) => {
    try {
        const { user_name, recipes } = req.body;

        if (!user_name || !recipes) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const result = await RecipeService.deleteRecipe(user_name, recipes);

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: "Recipe not found" });
        }

        res.json({ success: true, message: "Recipe deleted" });

    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Failed to delete recipe" });
    }
};
