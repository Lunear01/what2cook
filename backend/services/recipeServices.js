const db = require("../config/db");

const recipeServices = {
    async addRecipe(user_name, recipe_id, recipes){
        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [result] = await db.execute(
            "INSERT INTO recipe (user_id, recipe_id, recipes) VALUES (?, ?, ?)",
            [user_id, recipe_id, recipes]
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

    async deleteRecipe(user_name, recipe_id) {

        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [result] = await db.execute(
            "DELETE FROM recipe WHERE user_id = ? AND recipe_id = ?",
            [user_id, recipe_id]
        );

        return result;
    },

    async findRecipe(user_name, recipe_id){
        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [recipe_row] = await db.execute(
            "SELECT * FROM recipe WHERE user_id = ? AND recipe_id = ?",
            [user_id, recipe_id]
        );
        return recipe_row[0]
    }
};

module.exports = recipeServices;
