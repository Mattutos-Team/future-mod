package com.mattutos.future.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mattutos.future.init.ModRecipeSerializers;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class VarnishedWoodRecipe extends ShapelessRecipe {
    private final ItemStack result;

    public VarnishedWoodRecipe(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> inputs) {
        super(id, group, CraftingBookCategory.MISC, result, inputs);
        this.result = result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        var remaining = super.getRemainingItems(inv);

        for (int i = 0; i < inv.getContainerSize(); i++) {
            var stack = inv.getItem(i);

            if (stack.getItem().getDescriptionId().equals("item.future_mod.varnish")) {
                log.info("teste slf4j");
                var varnish = stack.copy();

                if (!varnish.hurt(1, new LegacyRandomSource(123), null)) {
                    remaining.set(i, varnish);
                }
            }
        }

        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.VARNISHED_WOOD.get();
    }

    public static class Serializer implements RecipeSerializer<VarnishedWoodRecipe> {

        @Override
        public VarnishedWoodRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            var group = GsonHelper.getAsString(json, "group", "");
            var ingredients = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));

            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else {
                var result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
                return new VarnishedWoodRecipe(recipeId, group, result, ingredients);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray array) {
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for (int i = 0; i < array.size(); ++i) {
                var ingredient = Ingredient.fromJson(array.get(i));

                if (!ingredient.isEmpty()) {
                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        }

        @Override
        public @Nullable VarnishedWoodRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            var group = buffer.readUtf(32767);
            int size = buffer.readVarInt();
            var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

            for (int j = 0; j < inputs.size(); ++j) {
                inputs.set(j, Ingredient.fromNetwork(buffer));
            }

            var result = buffer.readItem();

            return new VarnishedWoodRecipe(recipeId, group, result, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, VarnishedWoodRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (var ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
        }
    }

}
