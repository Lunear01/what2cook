require('dotenv').config();
const express = require('express');
const cors = require('cors');
const helmet = require('helmet');

const app = express();

app.use(helmet());
app.use(cors());
app.use(express.json());

app.use('/user', require('./routes/userRoutes'));
app.use('/ingredient', require('./routes/ingredientRoutes'));
//app.use('/recipe', require('./routes/recipeRoutes'));

// Health check
app.get('/', (req, res) => res.send('What2Cook backend is running!'));

app.use((err, req, res, next) => {
    console.error(err);
    res.status(err.status || 500).json({ error: err.message || 'Internal Server Error' });
});

// start
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});
