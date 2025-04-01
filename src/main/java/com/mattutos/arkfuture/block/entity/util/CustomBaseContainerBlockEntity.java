package com.mattutos.arkfuture.block.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class CustomBaseContainerBlockEntity extends BlockEntity implements Container, MenuProvider, Nameable {
    private LockCode lockKey = LockCode.NO_LOCK;
    @Nullable
    private Component name;

    protected CustomBaseContainerBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.lockKey = LockCode.fromTag(pTag);
        if (pTag.contains("CustomName", 8)) {
            this.name = parseCustomNameSafe(pTag.getString("CustomName"), pRegistries);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        this.lockKey.addToTag(pTag);
        if (this.name != null) {
            pTag.putString("CustomName", Component.Serializer.toJson(this.name, pRegistries));
        }
    }

    @Override
    public @NotNull Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    protected abstract Component getDefaultName();

    public boolean canOpen(Player pPlayer) {
        return canUnlock(pPlayer, this.lockKey, this.getDisplayName());
    }

    public static boolean canUnlock(Player pPlayer, LockCode pCode, Component pDisplayName) {
        if (!pPlayer.isSpectator() && !pCode.unlocksWith(pPlayer.getMainHandItem())) {
            pPlayer.displayClientMessage(Component.translatable("container.isLocked", pDisplayName), true);
            pPlayer.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return false;
        } else {
            return true;
        }
    }

    protected abstract ItemStackHandler getItems();

    protected abstract void setItems(ItemStackHandler pItems);

    @Override
    public boolean isEmpty() {
        for (int slot = 0; slot < this.getItems().getSlots(); slot++) {
            if (!this.getItems().getStackInSlot(slot).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int pSlot) {
        return this.getItems().getStackInSlot(pSlot);
    }

    @Override
    public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack itemstack = this.getItems().extractItem(pSlot, pAmount, false);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
        return this.getItems().extractItem(pSlot, this.getItems().getStackInSlot(pSlot).getCount(), false);
    }

    @Override
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        this.getItems().setStackInSlot(pSlot, pStack);
        pStack.limitSize(this.getMaxStackSize(pStack));
        this.setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        for (int slot = 0; slot < this.getItems().getSlots(); slot++) {
            this.getItems().setStackInSlot(slot, ItemStack.EMPTY);
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return this.canOpen(pPlayer) ? this.createMenu(pContainerId, pPlayerInventory) : null;
    }

    protected abstract AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory);

    private NonNullList<ItemStack> getItemsForDisplay() {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        for (int slot = 0; slot < this.getItems().getSlots(); slot++) {
            nonnulllist.add(this.getItems().getStackInSlot(slot));
        }

        return nonnulllist;
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.@NotNull DataComponentInput pComponentInput) {
        super.applyImplicitComponents(pComponentInput);
        this.name = pComponentInput.get(DataComponents.CUSTOM_NAME);
        this.lockKey = pComponentInput.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
        pComponentInput.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.getItemsForDisplay());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder pComponents) {
        super.collectImplicitComponents(pComponents);
        pComponents.set(DataComponents.CUSTOM_NAME, this.name);
        if (!this.lockKey.equals(LockCode.NO_LOCK)) {
            pComponents.set(DataComponents.LOCK, this.lockKey);
        }

        pComponents.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItemsForDisplay()));
    }

    @Override
    public void removeComponentsFromTag(@NotNull CompoundTag pTag) {
        pTag.remove("CustomName");
        pTag.remove("Lock");
        pTag.remove("Items");
    }

    private LazyOptional<?> itemHandler = LazyOptional.of(this::createUnSidedHandler);

    protected IItemHandler createUnSidedHandler() {
        return new InvWrapper(this);
    }

    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER && !this.remove)
            return itemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandler = LazyOptional.of(this::createUnSidedHandler);
    }
}
