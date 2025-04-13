package com.mattutos.arkfuture.mixins;

import com.mattutos.arkfuture.crafting.recipe.transform.TransformCircumstance;
import com.mattutos.arkfuture.crafting.recipe.transform.TransformLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Process transform recipes on explosion only.
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    private static final Logger log = LoggerFactory.getLogger(ItemEntityMixin.class);

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract ItemStack getItem();

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    void handleExplosion(DamageSource src, float dmg, CallbackInfoReturnable<Boolean> ci) {
        if (!level().isClientSide && src.is(DamageTypeTags.IS_EXPLOSION) && !isRemoved()) {
            var self = (ItemEntity) (Object) this;
            if (TransformLogic.canTransformInExplosion(self)
                    && TransformLogic.tryTransform(self, TransformCircumstance::isExplosion)) {
                ci.setReturnValue(false);
                ci.cancel();
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "tick")
    void tickHook(CallbackInfo ci) {
    }
}
