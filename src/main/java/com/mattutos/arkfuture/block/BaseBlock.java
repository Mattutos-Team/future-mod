package com.mattutos.arkfuture.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class BaseBlock extends Block {

    public BaseBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public BaseBlock(Function<Properties, Properties> properties) {
        super(properties.apply(Properties.of()));
    }

    public BaseBlock(MapColor color, SoundType sound, float hardness, float resistance, int lightLevel) {
        super(Properties.of().sound(sound).strength(hardness, resistance).mapColor(color).lightLevel((e) -> lightLevel));
    }

    public BaseBlock(MapColor color, SoundType sound, float hardness, float resistance) {
        super(Properties.of().sound(sound).strength(hardness, resistance).mapColor(color));
    }

    public BaseBlock(SoundType sound, float hardness, float resistance) {
        super(Properties.of().sound(sound).strength(hardness, resistance));
    }

    public BaseBlock(SoundType sound, float hardness, float resistance, boolean tool) {
        super(
                tool ? Properties.of().sound(sound).strength(hardness, resistance).requiresCorrectToolForDrops()
                        : Properties.of().sound(sound).strength(hardness, resistance)
        );
    }

    public BaseBlock(MapColor color, SoundType sound, float hardness, float resistance, boolean tool) {
        super(
                tool ? Properties.of().sound(sound).strength(hardness, resistance).mapColor(color).requiresCorrectToolForDrops()
                        : Properties.of().sound(sound).strength(hardness, resistance).mapColor(color)
        );
    }

    public abstract InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit);
}
