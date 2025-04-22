package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.util.AFEnergyContainerBlockEntity;
import com.mattutos.arkfuture.core.energy.AFEnergyStorage;
import com.mattutos.arkfuture.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class VitalEnergyCubeBlockEntity extends AFEnergyContainerBlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public VitalEnergyCubeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.VITAL_ENERGY_CUBE.get(), pPos, pBlockState);
    }

    @Override
    protected AFEnergyStorage getEnergyStorage() {
        return new AFEnergyStorage(0);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("FODASE");
    }

    @Override
    public ItemStackHandler getItemStackHandler() {
        return new ItemStackHandler(1);
    }

    @Override
    public void tickServer() {

    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
