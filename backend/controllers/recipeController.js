const recipeServices = require("../services/recipeServices");
const db = require("../config/db");

exports.addRecipe = async (req, res) => {
    try {
        const { user_name, recipe_name, recipe_id, recipes } = req.body;

        if (!user_name || !recipe_name || !recipe_id || !recipes) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const result = await recipeServices.addRecipe(user_name, recipe_name, recipe_id, recipes);

        res.json({
            success: true,
            message: "recipe added",
            Recipe: result
        });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: err.message || "Failed to add recipe" });
    }
};

exports.getRecipes = async (req, res) => {
    try {
        const { user_name } = req.params;

        const recipesList = await recipeServices.getRecipesByUser(user_name);

        res.json({ success: true, recipes: recipesList });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: err.message || "Failed to get recipes" });
    }
};

exports.deleteRecipe = async (req, res) => {
    try {
        const { user_name, recipe_name } = req.body;

        if (!user_name || !recipe_name) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const result = await recipeServices.deleteRecipe(user_name, recipe_name);

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: "Recipe not found" });
        }

        res.json({ success: true, message: "Recipe deleted" });

    } catch (err) {
        console.error(err);
        res.status(500).json({ error: err.message || "Failed to delete Recipe" });
    }
};
