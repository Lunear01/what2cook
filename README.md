# What2Cook
API infused recipe recommendation/search engine based on user provided ingredients, aiming to solve the long-troubling question of “What to cook for today?” Recipes are ranked based on calories and heathscores, providing users with calorie tracking or nutritional value needs with information according to the recipe being chosen.

### User Stories
- [x] 1. As a user, I want to create my account and login, so that I can track all my user activities.
- [x] 2. As a user, I want to add my ingredients to my fridge, so that I can easily track the ingredients I want to cook.
- [x] 3. As a user, I want to retrieve a list of relevant recipes with calorie information based on my input ingredients, so I can select my preferred option.
- [x] 4. As a user, I want to get the instructions and health score of my selected recipe so that I can cook it.
- [x] 5. As a user, I want to add a recipe to my personal cooking list, so that I can keep track of the dishes I cooked or plan to cook.
- [x] 6. As a user, I want to add recipes to my favorites list, so that I can easily find and revisit the recipes I like the most.
- [x] 7. As a user I want to have my user credentials and user information saved, so that I don’t have to create an account every time I want to login. 

### Backend Framework:
Client (Java APP) <--HTTP/JSON--> API Gateway (Express index.js)
├── routes/ (userRoutes, ingredientRoutes, recipeRoutes)
├── controllers/ (userController, ingredientController, recipeController)
├── services/ (userService, ingredientService, recipeService)
└── db.js (mysql2 connection pool)

MySQL Database (what2cook)
├── user / users table
├── ingredient (user's ingredient list)
└── recipe (user's saved recipes)

### Group Memebers:
- Haiwen Yi
- Iker Huang
- Yuchen Ji
- Mohan Yuan
- Han Kong
- David Kang-hee Kim
