package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.util.AFBaseContainerBlockEntity;
import com.mattutos.arkfuture.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AssemblerPartBlockEntity extends AFBaseContainerBlockEntity implements GeoBlockEntity {
    public static final String ENTITY_CORE_KEY = "EntityCorePos";
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final ItemStackHandler itemStackHandler;

    private MechanicalAssemblerBlockEntity entityCore;
    private BlockPos entityCorePos;

    public AssemblerPartBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.ASSEMBLER_PART.get(), pPos, pBlockState);

        itemStackHandler = createItemStackHandlerSingle();
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> animationState) {
        if (!this.getItem(0).equals(ItemStack.EMPTY) && this.entityCore != null && this.entityCore.isAssembling()) {
            animationState.getController().setAnimation(
                    RawAnimation.begin().then("animation.assembler_part.indicators", Animation.LoopType.LOOP)
            );

            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
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
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.entityCorePos = null;
        this.entityCore = null;
        if (pTag.contains(ENTITY_CORE_KEY)) {
            CompoundTag tagEntityCorePos = pTag.getCompound(ENTITY_CORE_KEY);
            this.entityCorePos = new BlockPos(tagEntityCorePos.getInt("x"), tagEntityCorePos.getInt("y"), tagEntityCorePos.getInt("z"));
            resetEntityCoreByPos();
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        if (this.entityCore != null) {
            CompoundTag tagEntityCorePos = new CompoundTag();
            tagEntityCorePos.putInt("x", this.entityCore.getBlockPos().getX());
            tagEntityCorePos.putInt("y", this.entityCore.getBlockPos().getY());
            tagEntityCorePos.putInt("z", this.entityCore.getBlockPos().getZ());
            pTag.put(ENTITY_CORE_KEY, tagEntityCorePos);
        }
    }

    @Override
    public void tickServer() {
        if (this.level == null) return;
        resetEntityCoreByPos();

        if (this.entityCore != null) {
            if (this.level.getBlockEntity(entityCore.getBlockPos()) instanceof MechanicalAssemblerBlockEntity mechanicalAssemblerBlockEntity) {
                this.entityCore = mechanicalAssemblerBlockEntity;
            } else {
                this.entityCore = null;
            }
            setChanged();
        }
        BlockState newBlockState = this.getBlockState().setValue(BlockStateProperties.POWERED, this.entityCore != null && this.entityCore.isAssembling());
        this.level.setBlock(this.worldPosition, newBlockState, Block.UPDATE_ALL);
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return null;
    }

    private void resetEntityCoreByPos() {
        if (this.entityCorePos == null) return;
        if (this.level != null && this.level.getBlockEntity(this.entityCorePos) instanceof MechanicalAssemblerBlockEntity mechanicalAssemblerBlockEntity) {
            this.entityCore = mechanicalAssemblerBlockEntity;
        }
        this.entityCorePos = null;
    }

    public void setEntityCore(MechanicalAssemblerBlockEntity entityCore) {
        this.entityCore = entityCore;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

}
