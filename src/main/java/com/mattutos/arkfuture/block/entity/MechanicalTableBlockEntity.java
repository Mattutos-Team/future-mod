package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.init.BlockEntityInit;
import com.mattutos.arkfuture.menu.MechanicalTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MechanicalTableBlockEntity extends BlockEntity implements MenuProvider {


    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public MechanicalTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.MECHANICAL_TABLE.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Mechanical Table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MechanicalTableMenu(pContainerId, pPlayerInventory, ContainerLevelAccess.create(pPlayer.level(), this.getBlockPos()));
    }


    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}
