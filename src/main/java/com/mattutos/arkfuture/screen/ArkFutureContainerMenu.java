package com.mattutos.arkfuture.screen;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;

public abstract class ArkFutureContainerMenu extends AbstractContainerMenu {

    protected final BlockEntity blockEntity;
    protected final ContainerData containerData;
    protected final Level level;

    protected ArkFutureContainerMenu(
            MenuType<?> pMenuType,
            int pContainerId,
            Inventory pPlayerInventory,
            BlockEntity pBlockEntity,
            ContainerData pContainerData
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

    protected void addPlayerInventorySlots(Container playerInventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.canInteractWithBlock(this.blockEntity.getBlockPos(), 4.0D);
    }

}
