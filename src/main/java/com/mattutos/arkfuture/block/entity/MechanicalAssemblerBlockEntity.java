package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.util.AFEnergyContainerBlockEntity;
import com.mattutos.arkfuture.core.energy.AFEnergyStorage;
import com.mattutos.arkfuture.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MechanicalAssemblerBlockEntity extends AFEnergyContainerBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final ItemStackHandler itemStackHandler;
    private final AFEnergyStorage energyStorage;

    public MechanicalAssemblerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.MECHANICAL_ASSEMBLER.get(), pPos, pBlockState);

        itemStackHandler = createItemStackHandler();
        energyStorage = createEnergyStorage(10_000);
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.mechanical_assembler.powered", Animation.LoopType.LOOP));
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
    protected AFEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.ark_future.mechanical_assembler");
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
