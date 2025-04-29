package com.mattutos.arkfuture.block;

import com.mattutos.arkfuture.block.entity.VitalEnergyCubeBlockEntity;
import com.mattutos.arkfuture.block.util.AFBaseEntityBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class VitalEnergyCubeBlock extends AFBaseEntityBlock {

    public static final MapCodec<VitalEnergyCubeBlock> CODEC = simpleCodec(VitalEnergyCubeBlock::new);

    public VitalEnergyCubeBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new VitalEnergyCubeBlockEntity(pPos, pState);
    }

}
