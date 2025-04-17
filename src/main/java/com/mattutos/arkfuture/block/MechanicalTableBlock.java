package com.mattutos.arkfuture.block;

import com.mattutos.arkfuture.block.entity.MechanicalTableBlockEntity;
import com.mattutos.arkfuture.block.util.AFBaseEntityBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MechanicalTableBlock extends AFBaseEntityBlock {

    public static final MapCodec<MechanicalTableBlock> CODEC = simpleCodec(MechanicalTableBlock::new);

    public MechanicalTableBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MechanicalTableBlockEntity(pPos, pState);
    }
}
