// This is a test that ensure the database is connected successfully

const db = require('./config/db');

async function testDB() {
    try {
        const [rows] = await db.query("SELECT NOW() AS now");
        console.log("Database connected successfully, time: ", rows[0].now);
    } catch (err) {
        console.error("Database failed to connect:", err);
    }
}

testDB();
