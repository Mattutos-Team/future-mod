package com.mattutos.arkfuture.crafting.recipe.transform;

import com.google.common.collect.Lists;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public class TransformLogic {

    static Set<Item> explosionCache = null;

    public static boolean canTransformInExplosion(ItemEntity entity) {
        return getTransformableItemsExplosion(entity.level()).contains(entity.getItem().getItem());
    }


    public static boolean tryTransform(ItemEntity entity, Predicate<TransformCircumstance> circumstancePredicate) {
        var level = entity.level();

        var region = new AABB(entity.getX() - 1, entity.getY() - 1, entity.getZ() - 1, entity.getX() + 1,
                entity.getY() + 1, entity.getZ() + 1);
        List<ItemEntity> itemEntities = level.getEntities(null, region).stream()
                .filter(e -> e instanceof ItemEntity && !e.isRemoved()).map(e -> (ItemEntity) e).toList();

        for (var holder : level.getRecipeManager().getAllRecipesFor(ModRecipe.TRANSFORM_RECIPE_TYPE.get())) {
            var recipe = holder.value();
            if (!circumstancePredicate.test(recipe.circumstance()))
                continue;

            if (recipe.ingredients().isEmpty())
                continue;

            List<Ingredient> missingIngredients = Lists.newArrayList(recipe.ingredients());
            Reference2IntMap<ItemEntity> consumedItems = new Reference2IntOpenHashMap<>(missingIngredients.size());

            if (recipe.circumstance().isExplosion()) {
                if (missingIngredients.stream().noneMatch(i -> i.test(entity.getItem())))
                    continue;
            } else {
                if (!missingIngredients.getFirst().test(entity.getItem()))
                    continue;
            }

            for (var itemEntity : itemEntities) {
                var other = itemEntity.getItem();
                if (!other.isEmpty()) {
                    for (var it = missingIngredients.iterator(); it.hasNext(); ) {
                        Ingredient ing = it.next();
                        var alreadyClaimed = consumedItems.getInt(itemEntity);
                        if (ing.test(other) && other.getCount() - alreadyClaimed > 0) {
                            consumedItems.merge(itemEntity, 1, Integer::sum);
                            it.remove();
                        }
                    }
                }
            }

            if (missingIngredients.isEmpty()) {
                var items = new ArrayList<ItemStack>(consumedItems.size());
                for (var e : consumedItems.reference2IntEntrySet()) {
                    var itemEntity = e.getKey();
                    items.add(itemEntity.getItem().split(e.getIntValue()));

                    if (itemEntity.getItem().getCount() <= 0) {
                        itemEntity.discard();
                    }
                }
                var recipeInput = new TransformRecipeInput(items);
                var craftResult = recipe.assemble(recipeInput, level.registryAccess());

                final ItemEntity newEntity = getItemEntity(entity, level, craftResult);
                level.addFreshEntity(newEntity);

                return true;
            }
        }

        return false;
    }

    private static @NotNull ItemEntity getItemEntity(ItemEntity entity, Level level, ItemStack craftResult) {
        var random = level.getRandom();
        final double x = Math.floor(entity.getX()) + .25d + random.nextDouble() * .5;
        final double y = Math.floor(entity.getY()) + .25d + random.nextDouble() * .5;
        final double z = Math.floor(entity.getZ()) + .25d + random.nextDouble() * .5;
        final double xSpeed = random.nextDouble() * .25 - 0.125;
        final double ySpeed = random.nextDouble() * .25 - 0.125;
        final double zSpeed = random.nextDouble() * .25 - 0.125;

        final ItemEntity newEntity = new ItemEntity(level, x, y, z, craftResult);

        newEntity.setDeltaMovement(xSpeed, ySpeed, zSpeed);
        return newEntity;
    }

    private static Set<Item> getTransformableItemsExplosion(Level level) {
        Set<Item> ret = explosionCache;
        if (ret == null) {
            ret = Collections.newSetFromMap(new IdentityHashMap<>());
            for (var holder : level.getRecipeManager().getAllRecipesFor(ModRecipe.TRANSFORM_RECIPE_TYPE.get())) {
                var recipe = holder.value();
                if (!recipe.circumstance().isExplosion())
                    continue;
                for (var ingredient : recipe.ingredients()) {
                    for (var stack : ingredient.getItems()) {
                        ret.add(stack.getItem());
                    }
                }
            }
            explosionCache = ret;
        }
        return ret;
    }
}
