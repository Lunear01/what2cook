const ingredientService = require("../services/ingredientServices");

exports.addIngredient = async (req, res) => {
    try {
        const { user_name, ingredient_name, ingredient_id } = req.body;

        if (!user_name || !ingredient_name || !ingredient_id ) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const result = await ingredientService.addIngredient(user_name, ingredient_name, ingredient_id);

        res.json({
            success: true,
            message: "Ingredient added",
            ingredient: result
        });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: err.message || "Failed to add ingredient" });
    }
};


exports.getIngredients = async (req, res) => {
    try {
        const { user_name } = req.params;

        const ingredientsList = await ingredientService.getIngredientsByUser(user_name);

        res.json({ success: true, ingredients: ingredientsList });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: err.message || "Failed to get ingredients" });
    }
};


exports.deleteIngredient = async (req, res) => {
    try {
        const { user_name, ingredient_id } = req.body;

        if (!user_name || !ingredient_id) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const result = await ingredientService.deleteIngredient(user_name, ingredient_id);

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: "Ingredient not found" });
        }

        res.json({ success: true, message: "Ingredient deleted" });

    } catch (err) {
        console.error(err);
        res.status(500).json({ error: err.message || "Failed to delete ingredient" });
    }
};
