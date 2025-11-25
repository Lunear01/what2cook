const userServices = require("../services/userServices");

exports.signup = async (req, res) => {
    const { user_name, email, password } = req.body;

    try {
        const exists = await userServices.findUser(user_name);
        if (exists) return res.status(400).json({ message: "User already exists" });

        await userServices.createUser(user_name, email, password);
        res.json({ message: "User registered" });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Server error" });
    }
};

exports.login = async (req, res) => {
    const { user_name, password } = req.body;

    try {
        const user = await userServices.findUser(user_name);
        if (!user) return res.status(400).json({ message: "User not found" });

        if (user.password !== password)
            return res.status(400).json({ message: "Wrong password" });

        res.json({ message: "Login success", user });
    } catch (err) {
        res.status(500).json({ error: "Server error" });
    }
};