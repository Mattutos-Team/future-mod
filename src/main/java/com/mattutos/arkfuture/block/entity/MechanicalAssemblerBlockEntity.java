package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.util.AFEnergyContainerBlockEntity;
import com.mattutos.arkfuture.core.energy.AFEnergyStorage;
import com.mattutos.arkfuture.init.BlockEntityInit;
import com.mattutos.arkfuture.init.BlockInit;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

@Slf4j
public class MechanicalAssemblerBlockEntity extends AFEnergyContainerBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final ItemStackHandler itemStackHandler;
    private final AFEnergyStorage energyStorage;

    public MechanicalAssemblerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.MECHANICAL_ASSEMBLER.get(), pPos, pBlockState);

        itemStackHandler = createItemStackHandlerSingle();
        energyStorage = createEnergyStorage(10_000);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.mechanical_assembler.powered", Animation.LoopType.LOOP));

        animationState.setData(DataTickets.ACTIVE, energyStorage.getEnergyStored() > 0);

        animationState.setData(DataTickets.ANIM_STATE, 1);

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

    private BlockPattern getMechanicalAssemblyPattern() {
        return BlockPatternBuilder.start()
                .aisle(" M ")
                .aisle("PPP")
                .aisle(" P ")
                .aisle(" P ")
                .where('P', BlockInWorld.hasState(blockInWorld -> blockInWorld.getBlock().equals(BlockInit.ASSEMBLER_PART.get())))
                .where('M', BlockInWorld.hasState(blockInWorld -> blockInWorld.getBlock().equals(BlockInit.MECHANICAL_ASSEMBLER.get())))
                .build();
    }

    public void tryExecuteProcess() {
        BlockPattern pattern = getMechanicalAssemblyPattern();
        BlockPattern.BlockPatternMatch blockPatternMatch = pattern.find(this.level, this.worldPosition);

        if (blockPatternMatch != null) {
            log.info("Mechanical Assembler Block Entity found pattern");
            Cow cow = EntityType.COW.create(level);
            if (cow != null) {
                // Define a posição para spawnar (por exemplo, acima da block entity)
                BlockPos spawnPos = worldPosition.above();
                cow.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0.0F, 0.0F);

                // Adiciona a entidade no mundo
                level.addFreshEntity(cow);
            }
        } else {
            log.warn("Mechanical Assembler Block Entity did not find pattern");
        }
    }

}
