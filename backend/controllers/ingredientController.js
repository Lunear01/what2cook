const IngredientsService = require("../services/ingredientServices");
const db = require("../config/db");

exports.addIngredient = async (req, res) => {
    try {
        const { user_name, ingredient_name, Ingredients } = req.body;

        if (!user_name || !ingredient_name || !Ingredients) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const result = await IngredientsService.addIngredient(user_name, ingredient_name, Ingredients);

        res.json({
            success: true,
            message: "Ingredient added",
            ingredientId: result.insertId
        });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: err.message || "Failed to add ingredient" });
    }
};


exports.getIngredients = async (req, res) => {
    try {
        const { user_name } = req.params;

        const ingredientsList = await IngredientsService.getIngredientsByUser(user_name);

        res.json({ success: true, ingredients: ingredientsList });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: err.message || "Failed to get ingredients" });
    }
};


exports.deleteIngredient = async (req, res) => {
    try {
        const { user_name, ingredient_name } = req.body;

        if (!user_name || !ingredient_name) {
            return res.status(400).json({ error: "Missing fields" });
        }

        const result = await IngredientsService.deleteIngredient(user_name, ingredient_name);

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: "Ingredient not found" });
        }

        res.json({ success: true, message: "Ingredient deleted" });

    } catch (err) {
        console.error(err);
        res.status(500).json({ error: err.message || "Failed to delete ingredient" });
    }
};
