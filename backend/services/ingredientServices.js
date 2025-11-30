const db = require("../config/db");

const ingredientServices = {
    async addIngredient(user_name, ingredient_name){
        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [result] = await db.execute(
            "INSERT INTO ingredient (user_id, ingredient_name) VALUES (?, ?)",
            [user_id, ingredient_name]
        );
        return result;
    },

    async getIngredientsByUser(user_name) {

        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [rows] = await db.execute(
            "SELECT ingredient_id, ingredient_name FROM ingredient WHERE user_id = ?",
            [user_id]
        );

        return rows.map(r => ({
            ingredient_id: r.ingredient_id,
            ingredient_name: r.ingredient_name
        }));
    },

    async deleteIngredient(user_name, ingredient_id) {

        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [result] = await db.execute(
            "DELETE FROM ingredient WHERE user_id = ? AND ingredient_id = ?",
            [user_id, ingredient_id]
        );

        return result;
    },

    async findIngredient(user_name, ingredient_id){
        const [userRows] = await db.execute(
            "SELECT user_id FROM user WHERE user_name = ?",
            [user_name]
        );

        if (userRows.length === 0) {
            throw new Error("User not found")
        }

        const user_id = userRows[0].user_id;

        const [ingredient_row] = await db.execute(
            "SELECT * FROM ingredient WHERE user_id = ? AND ingredient_id = ?",
            [user_id, ingredient_id]
        );
        return ingredient_row[0]
    }
};


module.exports = ingredientServices;
