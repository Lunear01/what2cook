const db = require("../config/db");

const ingredientServices = {
    async addIngredient(user_name, ingredient_name, Ingredients){
        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [result] = await db.execute(
            "INSERT INTO ingredient (user_id, ingredient_name, Ingredients) VALUES (?, ?, ?)",
            [user_id, ingredient_name, Ingredients]
        );
        return result;
    },

    async getIngredientsByUser(user_name) {

        const [userRows] = await mysql.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [rows] = await mysql.execute(
            "SELECT ingredient_id, Ingredients FROM ingredient WHERE user_id = ?",
            [user_id]
        );

        return rows.map(r => ({
            ingredient_id: r.ingredient_id,
            ingredients: r.Ingredients
        }));
    },

    //TODO
    // Modify it into delete according to the ingredient id
    async deleteIngredient(user_name, ingredient_name) {

        const [userRows] = await mysql.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [result] = await mysql.execute(
            "DELETE FROM ingredient WHERE user_id = ? AND ingredient_name = ?",
            [user_id, ingredient_name]
        );

        return result;
    }
};

module.exports = ingredientServices;
