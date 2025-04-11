package com.mattutos.arkfuture.block.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AFBaseContainerBlockEntity extends BlockEntity implements Container, MenuProvider, Nameable {
    private LockCode lockKey = LockCode.NO_LOCK;
    @Nullable
    private Component name;

    protected LazyOptional<IItemHandler> lazyItemHandler;

    protected AFBaseContainerBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        lazyItemHandler = LazyOptional.of(this::getItemStackHandler);
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

    public abstract ItemStackHandler getItemStackHandler();

    public abstract void tickServer();

    @Override
    public int getContainerSize() {
        return getItemStackHandler().getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int slot = 0; slot < this.getItemStackHandler().getSlots(); slot++) {
            if (!this.getItemStackHandler().getStackInSlot(slot).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int pSlot) {
        return this.getItemStackHandler().getStackInSlot(pSlot);
    }

    @Override
    public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack itemstack = this.getItemStackHandler().extractItem(pSlot, pAmount, false);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
        return this.getItemStackHandler().extractItem(pSlot, this.getItemStackHandler().getStackInSlot(pSlot).getCount(), false);
    }

    @Override
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        this.getItemStackHandler().setStackInSlot(pSlot, pStack);
        pStack.limitSize(this.getMaxStackSize(pStack));
        this.setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(getItemStackHandler().getSlots());
        for (int i = 0; i < getItemStackHandler().getSlots(); i++) {
            inv.setItem(i, getItemStackHandler().getStackInSlot(i));
        }

        if (this.level != null) Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    public void clearContent() {
        for (int slot = 0; slot < this.getItemStackHandler().getSlots(); slot++) {
            this.getItemStackHandler().setStackInSlot(slot, ItemStack.EMPTY);
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
        for (int slot = 0; slot < this.getItemStackHandler().getSlots(); slot++) {
            nonnulllist.add(this.getItemStackHandler().getStackInSlot(slot));
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

    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER && !this.remove)
            return lazyItemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        lazyItemHandler = LazyOptional.of(this::getItemStackHandler);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(this::getItemStackHandler);
    }

    /**
     * Metodo usado para sincronização
     * Define os dados que serão enviados para o cliente quando um chunk contendo o BlockEntity for carregado.
     * 🔹 Quando é chamado?
     * Quando o chunk que contém o BlockEntity é enviado para o cliente (exemplo: jogador se aproxima do bloco).
     * Ele envia um NBT inicial para sincronizar o estado do bloco com o cliente.
     * <p>
     * 🔹 Para que serve?
     * Mantém a sincronização inicial do estado do bloco ao carregar chunks.
     * Se o bloco tiver informações personalizadas (energia, inventário, etc.), elas são enviadas para o cliente.
     */
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /**
     * Metodo usado para sincronização
     * Controla a atualização do BlockEntity quando o seu estado muda dinamicamente sem precisar recarregar o chunk.
     * 🔹 Quando é chamado?
     * Quando algo muda no BlockEntity (por exemplo, um contador interno, carga de energia, temperatura de uma máquina, etc.).
     * Se o código chamar level.sendBlockUpdated(), ele força a atualização para os clientes.
     * <p>
     * 🔹 Para que serve?
     * Sincroniza mudanças no bloco sem precisar descarregar e recarregar o chunk.
     * Mantém o cliente atualizado sem causar lag, pois só envia dados necessários.
     * 🔹 Exemplo de uso: Se o BlockEntity gerencia um valor de energia, você pode chamar esse método para atualizar o cliente sempre que a energia mudar.
     */
    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

}
