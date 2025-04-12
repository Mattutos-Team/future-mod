package com.mattutos.arkfuture.menu;

import com.mattutos.arkfuture.core.inventory.BaseData;
import com.mattutos.arkfuture.core.inventory.EnumContainerData;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public abstract class AFAbstractContainerMenu<T extends Enum<T> & BaseData> extends AbstractContainerMenu {

    protected static final int HOTBAR_SLOT_COUNT = 9;
    protected static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    protected static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    protected static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    protected static final int VANILLA_FIRST_SLOT_INDEX = 0;
    protected static final int BE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    protected final BlockEntity blockEntity;
    protected final EnumContainerData<T> containerData;
    protected final Level level;

    protected AFAbstractContainerMenu(
            MenuType<?> pMenuType,
            int pContainerId,
            Inventory pPlayerInventory,
            BlockEntity pBlockEntity,
            EnumContainerData<T> pContainerData
    ) {
        super(pMenuType, pContainerId);
        this.blockEntity = pBlockEntity;
        this.containerData = pContainerData;
        this.level = pPlayerInventory.player.level();
    }

    protected static void checkItemHandlerCount(IItemHandler pIntArray, int pMinSize) {
        int i = pIntArray.getSlots();
        if (i < pMinSize) {
            throw new IllegalArgumentException("Item handler count " + i + " is smaller than expected " + pMinSize);
        }
    }

    private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void addPlayerInventorySlots(Container playerInventory) {
        this.addPlayerInventorySlots(playerInventory, 8, 84);
    }

    protected void addPlayerInventorySlots(Container playerInventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.canInteractWithBlock(this.blockEntity.getBlockPos(), 0);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            // Se o item vem do inventario do jogador
            if (pIndex < BE_INVENTORY_FIRST_SLOT_INDEX) {
                if (!this.moveItemStackTo(stackInSlot, BE_INVENTORY_FIRST_SLOT_INDEX, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            // Se o item vem do inventario do bloco
            else {
                if (!this.moveItemStackTo(stackInSlot, VANILLA_FIRST_SLOT_INDEX, VANILLA_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            slot.onTake(pPlayer, stackInSlot);
        }

        return itemstack;
    }

    @Override
    protected boolean moveItemStackTo(@NotNull ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
        boolean flag = false;
        int i = pStartIndex;
        if (pReverseDirection) {
            i = pEndIndex - 1;
        }

        if (pStack.isStackable()) {
            while (!pStack.isEmpty() && (pReverseDirection ? i >= pStartIndex : i < pEndIndex)) {
                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameComponents(pStack, itemstack) && slot.mayPlace(pStack)) {
                    int j = itemstack.getCount() + pStack.getCount();
                    int k = Math.min(slot.getMaxStackSize(itemstack), itemstack.getMaxStackSize());
                    if (j <= k) {
                        pStack.setCount(0);
                        itemstack.setCount(j);
                        slot.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < k) {
                        pStack.shrink(k - itemstack.getCount());
                        itemstack.setCount(k);
                        slot.setChanged();
                        flag = true;
                    }
                }

                if (pReverseDirection) {
                    i--;
                } else {
                    i++;
                }
            }
        }

        if (!pStack.isEmpty()) {
            if (pReverseDirection) {
                i = pEndIndex - 1;
            } else {
                i = pStartIndex;
            }

            while (pReverseDirection ? i >= pStartIndex : i < pEndIndex) {
                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(pStack)) {
                    int l = slot1.getMaxStackSize(pStack);
                    slot1.setByPlayer(pStack.split(Math.min(pStack.getCount(), l)));
                    slot1.setChanged();
                    flag = true;
                    break;
                }

                if (pReverseDirection) {
                    i--;
                } else {
                    i++;
                }
            }
        }

        return flag;
    }


}
