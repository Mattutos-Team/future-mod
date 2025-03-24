package com.mattutos.arkfuture.block.custom;

import com.mattutos.arkfuture.block.BaseBlock;
import com.mattutos.arkfuture.menu.MechanicalTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
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
}
