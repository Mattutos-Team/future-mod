package com.mattutos.arkfuture.block.custom;

import com.mattutos.arkfuture.block.BaseBlock;
import com.mattutos.arkfuture.menu.MechanicalTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class MechanicalTableBlock extends BaseBlock {
    private static final Component CONTAINER_TITLE = Component.translatable("");

    public MechanicalTableBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.5F)
                .sound(SoundType.GLASS).instabreak());
    }

    @Override
    public MenuProvider getMenuProvider(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos) {
        return new SimpleMenuProvider((i, inventory, player) -> {
            return new MechanicalTableMenu(i, inventory, ContainerLevelAccess.create(pLevel, pPos));
        }, CONTAINER_TITLE);
    }

    @Override
    public InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
            pPlayer.awardStat(Stats.INTERACT_WITH_SMITHING_TABLE);
            return InteractionResult.CONSUME;
        }
    }
}
