package com.mattutos.arkfuture.screen;

import com.mattutos.arkfuture.block.entity.CoalPowerGeneratorBlockEntity;
import com.mattutos.arkfuture.init.MenuInit;
import com.mattutos.arkfuture.screen.util.EnergySlot;
import com.mattutos.arkfuture.screen.util.FuelSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class CoalPowerGeneratorMenu extends ArkFutureContainerMenu {

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int BE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    public CoalPowerGeneratorMenu(int i, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(i, inventory, new SimpleContainer(CoalPowerGeneratorBlockEntity.SLOT.count()), new SimpleContainerData(CoalPowerGeneratorBlockEntity.DATA.count()));
    }

    public CoalPowerGeneratorMenu(int pContainerId, Inventory inventory, Container pContainer, ContainerData pContainerData) {
        super(MenuInit.COAL_POWER_GENERATOR_MENU.get(), pContainerId, inventory, pContainer, pContainerData);

        checkContainerSize(pContainer, CoalPowerGeneratorBlockEntity.SLOT.count());
        checkContainerDataCount(pContainerData, CoalPowerGeneratorBlockEntity.DATA.count());

        addPlayerInventorySlots(inventory, 8, 84);

        this.addSlot(new FuelSlot(this.container, CoalPowerGeneratorBlockEntity.SLOT.FUEL.ordinal(), 26, 49));
        this.addSlot(new EnergySlot(this.container, CoalPowerGeneratorBlockEntity.SLOT.ENERGY_IN.ordinal(), 134, 16));
        this.addSlot(new EnergySlot(this.container, CoalPowerGeneratorBlockEntity.SLOT.ENERGY_OUT.ordinal(), 134, 49));
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

    public int getData(CoalPowerGeneratorBlockEntity.DATA data) {
        return this.data.get(data.ordinal());
    }

}
