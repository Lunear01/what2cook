package dataaccess;

import entity.Ingredient;
import entity.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeDataAccessImpl implements RecipeDataAccess {

    // 后端 Recipe 路由的基础 URL
    private static final String BASE_URL = "http://localhost:3000/recipe";

    /**
     * 向后端添加一个 recipe 记录。
     *
     * @param user_name 当前用户
     * @param recipeID  配方 id
     * @param recipes   这个配方的 JSON 表示（由上层构造）
     */
    @Override
    public void addRecipe(String user_name, int recipeID, JSONObject recipes) throws Exception {
        URL url = new URL(BASE_URL + "/add");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // 请求体：包含 user_name、recipeID 和 recipe JSON
        JSONObject body = new JSONObject();
        body.put("user_name", user_name);
        body.put("recipeID", recipeID);
        body.put("recipe", recipes);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes());
            os.flush();
        }

        // 触发请求并简单消费响应
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            while (br.readLine() != null) {
                // ignore content
                ;
            }
        }

        conn.disconnect();
    }

    /**
     * 从后端获取该用户所有 recipe，并转换成 List&lt;Recipe&gt; 实体。
     */
    @Override
    public List<Recipe> getAllRecipes(String user_name) throws Exception {
        List<Recipe> recipeList = new ArrayList<>();

        URL url = new URL(BASE_URL + "/" + user_name);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }

        conn.disconnect();

        // 假设服务器返回的是 JSON 数组：
        // [
        //   {
        //     "recipeID": 1,
        //     "title": "...",
        //     "image": "...",
        //     "ingredientNames": [
        //        {"name":"egg","id":1},
        //        ...
        //     ],
        //     "calories": 123.0,
        //     "healthScore": 80,
        //     "instructions": "..."
        //   },
        //   ...
        // ]
        JSONArray recipesArray = new JSONArray(sb.toString());

        for (int i = 0; i < recipesArray.length(); i++) {
            JSONObject obj = recipesArray.getJSONObject(i);

            List<Ingredient> ingredients = parseIngredientNames(obj);

            Recipe recipe = Recipe.builder()
                    .setId(obj.getInt("recipeID"))
                    .setTitle(obj.optString("title", ""))
                    .setImage(obj.optString("image", null))
                    .setIngredientNames(ingredients)
                    .setCalories(obj.optDouble("calories", 0.0))
                    .setHealthScore(obj.optInt("healthScore", 0))
                    .setInstructions(obj.optString("instructions", ""))
                    .build();

            recipeList.add(recipe);
        }

        return recipeList;
    }

    /**
     * 删除指定用户的某个 recipe。
     */
    @Override
    public void deleteRecipe(String user_name, int recipeID) throws Exception {
        URL url = new URL(BASE_URL + "/delete");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject body = new JSONObject();
        body.put("user_name", user_name);
        body.put("recipeID", recipeID);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes());
            os.flush();
        }

        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            while (br.readLine() != null) {
                // ignore content
                ;
            }
        }

        conn.disconnect();
    }

    /**
     * 把 recipe JSON 里的 "ingredientNames" 字段解析成 List&lt;Ingredient&gt;。
     *
     * 期望格式：
     * "ingredientNames": [
     *   { "name": "egg", "id": 1 },
     *   { "name": "milk", "id": 2 }
     * ]
     */
    private List<Ingredient> parseIngredientNames(JSONObject obj) {
        List<Ingredient> list = new ArrayList<>();

        JSONArray arr = obj.optJSONArray("ingredientNames");
        if (arr == null) {
            return list;
        }

        for (int i = 0; i < arr.length(); i++) {
            JSONObject ingObj = arr.getJSONObject(i);

            Ingredient ingredient = Ingredient.builder()
                    .setName(ingObj.getString("name"))
                    .setId(ingObj.optInt("id", -1))
                    .build();

            list.add(ingredient);
        }

        return list;
    }
}
