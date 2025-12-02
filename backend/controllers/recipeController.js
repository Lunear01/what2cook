const recipeServices = require("../services/recipeServices");

exports.addRecipe = async (req, res) => {
    try {
        const { user_name, recipe_id, recipes } = req.body;

        if (!user_name || !recipe_id || !recipes) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const result = await recipeServices.addRecipe(user_name, recipe_id, recipes);

        res.json({
            success: true,
            message: "recipe added",
            recipe_id: result.recipe_id,
            recipes: result.recipes
        });
    } catch (err) {
        console.error(err);
        res.status(500).json({ success: false, error: err.message || "Failed to add recipe" });
    }
};

exports.getRecipes = async (req, res) => {
    try {
        const { user_name } = req.params;

        const recipesList = await recipeServices.getRecipesByUser(user_name);

        res.json({ success: true, recipes: recipesList });
    } catch (err) {
        console.error(err);
        res.status(500).json({ success: false, error: err.message || "Failed to get recipes" });
    }
};

exports.deleteRecipe = async (req, res) => {
    try {
        const { user_name, recipe_id } = req.body;

        if (!user_name || !recipe_id) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const result = await recipeServices.deleteRecipe(user_name, recipe_id);

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: "Recipe not found" });
        }

        res.json({ success: true, message: "Recipe deleted" });

    } catch (err) {
        console.error(err);
        res.status(500).json({ success: false, error: err.message || "Failed to delete Recipe" });
    }
};


exports.exists = async (req, res) => {
    try {
        const { user_name, recipe_id } = req.params;

        if (!user_name || !recipe_id) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const result = await recipeServices.findRecipe(user_name, recipe_id);
 
        return res.json({ exists: !!result });

    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Server error" });
    }
};