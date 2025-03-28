package com.mattutos.arkfuture.block;

import com.mattutos.arkfuture.block.entity.CoalPowerGeneratorBlockEntity;
import com.mattutos.arkfuture.block.entity.MechanicalTableBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MechanicalTableBlock extends BaseEntityBlock {

    public static final MapCodec<MechanicalTableBlock> CODEC = simpleCodec(MechanicalTableBlock::new);

    public MechanicalTableBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MechanicalTableBlockEntity(pPos, pState);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof MechanicalTableBlockEntity blockEntity) {
                ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(blockEntity, Component.translatable("block.ark_future.mechanical_table")), pPos);
            } else {
                throw new IllegalStateException("The container provider is missing");
            }
        }

        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }
}
