package com.mattutos.arkfuture.menu;

import com.mattutos.arkfuture.block.entity.CoalPowerGeneratorBlockEntity;
import com.mattutos.arkfuture.block.entity.CoalPowerGeneratorBlockEntity.DATA;
import com.mattutos.arkfuture.block.entity.CoalPowerGeneratorBlockEntity.SLOT;
import com.mattutos.arkfuture.core.inventory.EnumContainerData;
import com.mattutos.arkfuture.core.inventory.SimpleEnumContainerData;
import com.mattutos.arkfuture.init.MenuInit;
import com.mattutos.arkfuture.menu.common.EnergySlot;
import com.mattutos.arkfuture.menu.common.FuelSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;

public class CoalPowerGeneratorMenu extends AFAbstractContainerMenu<DATA> {

    // construtor utilizado no lado do cliente
    public CoalPowerGeneratorMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, playerInventory, playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleEnumContainerData<>(DATA.class));
    }

    // construtor utilizado no lado do servidor
    public CoalPowerGeneratorMenu(int pContainerId, Inventory pPlayerInventory, BlockEntity pBlockEntity, EnumContainerData<DATA> pContainerData) {
        super(MenuInit.COAL_POWER_GENERATOR_MENU.get(), pContainerId, pPlayerInventory, pBlockEntity, pContainerData);
        IItemHandler itemHandler = ((CoalPowerGeneratorBlockEntity) pBlockEntity).getItemStackHandler();

        checkItemHandlerCount(itemHandler, SLOT.count());

        addPlayerInventorySlots(pPlayerInventory);

        this.addSlot(new FuelSlot(itemHandler, SLOT.FUEL.ordinal(), 26, 49));
        this.addSlot(new EnergySlot(itemHandler, SLOT.ENERGY_CHARGER.ordinal(), 134, 15));
        this.addSlot(new EnergySlot(itemHandler, SLOT.ENERGY_DISCHARGER.ordinal(), 134, 49));

        this.addDataSlots(pContainerData);
    }

    public boolean isBurning() {
        return this.containerData.get(DATA.REMAINING_BURN_TIME) > 0;
    }

    public float getScaledFlameProgress() {
        long progress = this.containerData.get(DATA.REMAINING_BURN_TIME);
        long maxProgress = this.containerData.get(DATA.TOTAL_BURN_TIME);

        return maxProgress != 0 && progress != 0 ? ((float) progress / (float) maxProgress) : 0;
    }

    public int getGenerating() {
        return (int) this.containerData.get(DATA.GENERATING);
    }

    public long getEnergyStored() {
        return this.containerData.get(DATA.ENERGY_STORED);
    }

    public long getMaxEnergyStored() {
        return this.containerData.get(DATA.CAPACITY);
    }

}
