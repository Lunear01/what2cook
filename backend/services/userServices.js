const db = require("../config/db");

const UserServices = {
    async findUser(user_name){
        const [user_rows] = await db.execute(
            "SELECT * FROM user WHERE user_name = ?",
            [user_name]
        );
        return user_rows[0]
    },

    async createUser(user_name, email, password){
        await db.execute(
            "INSERT INTO user (user_name, email, password) VALUES (?, ?, ?)",
            [user_name, email, password]
        );
    }
};

module.exports = UserServices;