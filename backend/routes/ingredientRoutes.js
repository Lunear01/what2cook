const express = require("express");
const router = express.Router();
const ingredientController = require("../controllers/ingredientController");

router.post("/add", ingredientController.addIngredient);

router.get("/:user_name", ingredientController.getIngredients);

router.delete("/delete", ingredientController.deleteIngredient);

module.exports = router;
