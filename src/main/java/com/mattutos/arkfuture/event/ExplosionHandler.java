package com.mattutos.arkfuture.event;

import com.mattutos.arkfuture.crafting.recipe.explosion.ExplosionRecipe;
import com.mattutos.arkfuture.crafting.recipe.explosion.ExplosionRecipeInput;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExplosionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExplosionHandler.class);

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        Level level = event.getLevel();
        if (level.isClientSide) return;

        Vec3 center = event.getExplosion().center();

        List<ItemStack> nearbyItems = event.getAffectedEntities().stream()
                .filter(entity -> entity instanceof ItemEntity)
                .map(entity -> (ItemEntity) entity)
                .map(ItemEntity::getItem)
                .toList();

        ExplosionRecipeInput input = new ExplosionRecipeInput(nearbyItems);

        List<RecipeHolder<ExplosionRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipe.EXPLOSION_RECIPE_TYPE.get());
        for (RecipeHolder<ExplosionRecipe> holder : recipes) {
            ExplosionRecipe recipe = holder.value();

            if (recipe.matches(input, level)) {
                ItemStack result = recipe.assemble(input, level.registryAccess());

                int resultCount = result.getCount();

                int smallestIngredientCount = input.ingredients().stream()
                        .map(ItemStack::getCount)
                        .mapToInt(Integer::intValue)
                        .min()
                        .orElse(0);

                ItemStack stack = new ItemStack(result.getItem(), resultCount * smallestIngredientCount);
                level.addFreshEntity(new ItemEntity(level, center.x, center.y, center.z, stack));
            } else {
                log.info("Recipe did not match: {}", recipe);
            }
        }
    }
}
