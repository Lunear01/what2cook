# What2Cook
API infused recipe recommendation/search engine based on user provided ingredients, aiming to solve the long-troubling question of “What to cook for today?” Recipes are ranked based on calories and heathscores, providing users with calorie tracking or nutritional value needs with information according to the recipe being chosen.

### User Stories
- [x] 1. As a user, I want to create my account, so that I can track all my user activities.
- [x] 2. As a user, I want to add my ingredients to my list, so that I can easily track the ingredients I want to cook.
- [x] 3. As a user, I want to get a list of relevant recipes, so that I can pick my preferred recipe.
- [x] 4. As a user, I want to get the instructions of my selected recipe so that I can cook it.
- [x] 5. As a user, I want to calculate the total calories for each dish based on the ingredients I input, so that I can monitor my overall calorie intake when choosing what to cook.
- [x] 6. As a user, I want to get the health score for each dish, so that I can understand the nutritional value of the foods I have.
- [ ] 7. As a user, I want to add a recipe to my personal cooking list, so that I can keep track of the dishes I plan to cook.
- [ ] 8. As a user, I want to add recipes to my favorites list, so that I can easily find and revisit the recipes I like the most.
- [x] 9. As a user, I want to search my recipes without waiting for so long, so I can save more time in cooking the recipes.

### Backend Framework:
Client (Java APP / Android) <--HTTP/JSON--> API Gateway (Express index.js)
├── routes/ (userRoutes, ingredientRoutes, recipeRoutes)
├── controllers/ (userController, ingredientController, recipeController)
├── services/ (userService, ingredientService, recipeService)
└── db.js (mysql2 connection pool)

MySQL Database (what2cook)
├── users / users table
├── ingredients (master list)
├── user_ingredients (user's ingredient list)
└── user_recipes (user's saved recipes)

### Group Memebers:
- Haiwen Yi
- Iker Huang
- Yuchen Ji
- Mohan Yuan
- Han Kong
- David Kang-hee Kim
