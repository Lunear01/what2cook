package entity;

/**
 * Builder for Ingredient entities.
 *
 * - name 必须非空
 * - ingredientId 可以为任意 int（包括 -1，表示“未知 / 占位”）
 */
public class IngredientBuilder {

    private String name;
    /** 默认为 -1，表示还没有真实的 id */
    private int ingredientId = -1;

    /**
     * Set the name for this IngredientBuilder.
     *
     * @param name the ingredient's name
     * @return this builder
     */
    public IngredientBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the id for this IngredientBuilder.
     * 可以传入 Spoonacular 返回的真实 id，
     * 也可以传 -1 作为占位（例如用户只输入了名字）。
     *
     * @param ingredientId the ingredient's id
     * @return this builder
     */
    public IngredientBuilder setId(int ingredientId) {
        this.ingredientId = ingredientId;
        return this;
    }

    /**
     * Build the Ingredient instance.
     *
     * @return new Ingredient
     * @throws IllegalStateException if name is missing or blank
     */
    public Ingredient build() {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("Ingredient name is required");
        }

        // 不再强制检查 ingredientId != -1，
        // -1 表示“未知 id”，是合法占位值。
        return new Ingredient(name, ingredientId);
    }
}
