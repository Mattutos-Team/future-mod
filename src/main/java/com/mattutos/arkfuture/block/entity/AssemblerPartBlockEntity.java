package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.util.AFBaseContainerBlockEntity;
import com.mattutos.arkfuture.init.BlockEntityInit;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

@Slf4j
public class AssemblerPartBlockEntity extends AFBaseContainerBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final ItemStackHandler itemStackHandler;

    public AssemblerPartBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.ASSEMBLER_PART.get(), pPos, pBlockState);

        itemStackHandler = createItemStackHandlerSingle();
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> animationState) {
//        animationState.getController().setAnimation(RawAnimation.begin().then("animation.assembler_part.indicators", Animation.LoopType.LOOP));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.ark_future.assembler_part");
    }

    @Override
    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    @Override
    public void tickServer() {
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return null;
    }

}
