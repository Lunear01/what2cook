const express = require("express");
const router = express.Router();
const RecipeController = require("../controllers/RecipeController");

router.post("/add", RecipeController.addRecipe);

router.get("/:user_name", RecipeController.getRecipes);

router.delete("/delete", RecipeController.deleteRecipe);

module.exports = router;
