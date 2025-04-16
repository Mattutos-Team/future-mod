package com.mattutos.arkfuture.block;

import com.mattutos.arkfuture.block.entity.AssemblerPartBlockEntity;
import com.mattutos.arkfuture.block.util.AFAssemblerBaseEntityBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AssemblerPartBlock extends AFAssemblerBaseEntityBlock {
    public static final MapCodec<AssemblerPartBlock> CODEC = simpleCodec(AssemblerPartBlock::new);

    public AssemblerPartBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AssemblerPartBlockEntity(pPos, pState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

}
