package com.mattutos.arkfuture.menu;

import com.mattutos.arkfuture.block.entity.CoalPowerGeneratorBlockEntity;
import com.mattutos.arkfuture.core.inventory.EContainerData;
import com.mattutos.arkfuture.core.inventory.SimpleEnumContainerData;
import com.mattutos.arkfuture.init.MenuInit;
import com.mattutos.arkfuture.screen.common.EnergySlot;
import com.mattutos.arkfuture.screen.common.FuelSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;

public class CoalPowerGeneratorMenu extends ArkFutureContainerMenu<CoalPowerGeneratorBlockEntity.DATA> {

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int BE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    protected final IItemHandler itemHandler;

    // construtor utilizado no lado do cliente
    public CoalPowerGeneratorMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, playerInventory, playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleEnumContainerData<>(CoalPowerGeneratorBlockEntity.DATA.class));
    }

    // construtor utilizado no lado do servidor
    public CoalPowerGeneratorMenu(int pContainerId, Inventory pPlayerInventory, BlockEntity pBlockEntity, EContainerData<CoalPowerGeneratorBlockEntity.DATA> pContainerData) {
        super(MenuInit.COAL_POWER_GENERATOR_MENU.get(), pContainerId, pPlayerInventory, pBlockEntity, pContainerData);
        this.itemHandler = ((CoalPowerGeneratorBlockEntity) pBlockEntity).getItems();

        checkItemHandlerCount(this.itemHandler, CoalPowerGeneratorBlockEntity.SLOT.count());
//        checkContainerDataCount(pContainerData, CoalPowerGeneratorBlockEntity.DATA.count());

        addPlayerInventorySlots(pPlayerInventory, 8, 84);

        this.addSlot(new FuelSlot(this.itemHandler, CoalPowerGeneratorBlockEntity.SLOT.FUEL.ordinal(), 26, 49));
        this.addSlot(new EnergySlot(this.itemHandler, CoalPowerGeneratorBlockEntity.SLOT.ENERGY_IN.ordinal(), 134, 15));
        this.addSlot(new EnergySlot(this.itemHandler, CoalPowerGeneratorBlockEntity.SLOT.ENERGY_OUT.ordinal(), 134, 49));

        this.addDataSlots(pContainerData);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        // TODO: terminar implementação
//        ItemStack itemstack = ItemStack.EMPTY;
//        Slot slot = this.slots.get(pIndex);
//        if (slot.hasItem()) {
//            ItemStack slotItemClicked = slot.getItem();
//            itemstack = slotItemClicked.copy();
//            if (pIndex < data.getCount()) {
//                if (!this.moveItemStackTo(slotItemClicked, data.getCount(), this.slots.size() + data.getCount(), true)) {
//                    return ItemStack.EMPTY;
//                }
//
//                slot.onQuickCraft(slotItemClicked, itemstack);
//            } else {
//
//            }
//
//
//
//            slot.onTake(pPlayer, slotItemClicked);
//        }
//
//        return itemstack;
        return ItemStack.EMPTY;
    }

    public long getData(CoalPowerGeneratorBlockEntity.DATA data) {
        return this.containerData.get(data);
    }

    public boolean isBurning() {
        return this.containerData.get(CoalPowerGeneratorBlockEntity.DATA.REMAINING_BURN_TIME) > 0;
    }

    public int getScaledFlameProgress(int pixelSize) {
        long progress = this.containerData.get(CoalPowerGeneratorBlockEntity.DATA.REMAINING_BURN_TIME);
        long maxProgress = this.containerData.get(CoalPowerGeneratorBlockEntity.DATA.TOTAL_BURN_TIME);

        return maxProgress != 0 && progress != 0 ? (int) (progress * pixelSize / maxProgress) : 0;
    }

    public int getGenerating() {
        return (int) this.containerData.get(CoalPowerGeneratorBlockEntity.DATA.GENERATING);
    }

    public long getEnergyStored() {
        return this.containerData.get(CoalPowerGeneratorBlockEntity.DATA.ENERGY_STORED);
    }

    public long getMaxEnergyStored() {
        return this.containerData.get(CoalPowerGeneratorBlockEntity.DATA.CAPACITY);
    }

}
