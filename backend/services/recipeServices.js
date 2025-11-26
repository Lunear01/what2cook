const db = require("../config/db");

const recipeServices = {
    async addRecipe(user_name, recipe_name, recipes){
        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [result] = await db.execute(
            "INSERT INTO recipe (user_id, recipe_name, recipes) VALUES (?, ?, ?)",
            [user_id, recipe_name, recipes]
        );
        return result;
    },

    async getRecipesByUser(user_name) {

        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [rows] = await db.execute(
            "SELECT recipe_id, recipes FROM recipe WHERE user_id = ?",
            [user_id]
        );

        return rows.map(r => ({
            recipe_id: r.recipe_id,
            recipes: r.recipes
        }));
    },

    //TODO
    // Modify it into delete according to the recipe id
    async deleteRecipe(user_name, recipe_name) {

        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [result] = await db.execute(
            "DELETE FROM recipe WHERE user_id = ? AND recipe_name = ?",
            [user_id, recipe_name]
        );

        return result;
    }
};

module.exports = recipeServices;
