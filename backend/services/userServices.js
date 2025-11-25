const db = require("../config/db");

exports.findUser = async (user_name) => {
    const [user_rows] = await db.execute(
        "SELECT * FROM user WHERE user_name = ?",
        [user_name]
    );
    return user_rows[0]
};

exports.createUser = async (user_name, email, password) => {
    await db.execute(
        "INSERT INTO user (user_name, email, password) VALUES (?, ?, ?)",
        [user_name, email, password]
    );
};