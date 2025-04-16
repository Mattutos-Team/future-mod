package com.mattutos.arkfuture.block.util;

import com.mattutos.arkfuture.block.entity.util.AFBaseContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AFAssemblerBaseEntityBlock extends AFBaseEntityBlock {
    protected AFAssemblerBaseEntityBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {

        if (pLevel.getBlockEntity(pPos) instanceof AFBaseContainerBlockEntity assemblerPartBlockEntity) {
            if (assemblerPartBlockEntity.getItem(0).isEmpty() && !pStack.isEmpty()) {
                assemblerPartBlockEntity.getItemStackHandler().insertItem(0, pStack.copyWithCount(1), false);
                pStack.shrink(1);
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            } else if (pStack.isEmpty()) {
                ItemStack stackOnAssemblerPart = assemblerPartBlockEntity.getItemStackHandler().extractItem(0, 1, false);
                if (!stackOnAssemblerPart.isEmpty()) {
                    pPlayer.getInventory().add(stackOnAssemblerPart);
                    assemblerPartBlockEntity.clearContent();
                    pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                }
            }
        }

        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

}
