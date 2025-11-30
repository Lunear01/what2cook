const express = require("express");
const router = express.Router();
const RecipeController = require("../controllers/RecipeController");
const ingredientController = require("../controllers/ingredientController");

router.post("/add", RecipeController.addRecipe);

router.get("/:user_name", RecipeController.getRecipes);

router.delete("/delete", RecipeController.deleteRecipe);

router.get("/:user_name/exists/:ingredient_id", ingredientController.exists);

module.exports = router;
