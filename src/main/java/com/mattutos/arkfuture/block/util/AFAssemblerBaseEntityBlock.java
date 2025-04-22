package com.mattutos.arkfuture.block.util;

import com.mattutos.arkfuture.block.entity.util.AFBaseContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AFAssemblerBaseEntityBlock extends AFBaseEntityBlock {
    protected AFAssemblerBaseEntityBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.POWERED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(BlockStateProperties.POWERED);
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

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(BlockStateProperties.POWERED)) {
            BlockPos relative = pPos.relative(Direction.UP);
            double dx1 = (double) relative.getX() + 0.5;
            double dy1 = (double) relative.getY() + 0.1;
            double dz1 = (double) relative.getZ() + 0.5;

            if (pRandom.nextDouble() < 0.2) {
                pLevel.playLocalSound(dx1, dy1, dz1, SoundEvents.VAULT_HIT, SoundSource.BLOCKS, 1f, 1f, false);
            }

            double dx2 = pRandom.nextDouble() * 0.6 - 0.3;
            double dy2 = 0;
            double dz2 = pRandom.nextDouble() * 0.6 - 0.3;

            pLevel.addParticle(
                    ParticleTypes.CRIT,
                    dx1 + dx2,
                    dy1 + dy2,
                    dz1 + dz2,
                    0.0D, 0.0D, 0.0D
            );
        }
    }
}
