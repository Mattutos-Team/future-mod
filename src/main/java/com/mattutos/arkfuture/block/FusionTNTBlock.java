package com.mattutos.arkfuture.block;

import com.mattutos.arkfuture.block.entity.FusionTNTPrimedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;


public class FusionTNTBlock extends Block {

    //BLOCK SHAPE
    private static final VoxelShape SHAPE = Shapes
            .create(new AABB(0.25f, 0.0f, 0.25f, 0.75f, 0.5f, 0.75f));

    public FusionTNTBlock(Properties pProperties) {
        super(pProperties);
    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pStack.is(Items.FLINT_AND_STEEL) || pStack.is(Items.FIRE_CHARGE)) {
            onCaughtFire(pState, pLevel, pPos, pHitResult.getDirection(), pPlayer);
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            Item item = pStack.getItem();

            if (!pPlayer.isCreative()) {
                if (pStack.is(Items.FLINT_AND_STEEL)) {
                    pStack.hurtAndBreak(1, pPlayer, LivingEntity.getSlotForHand(pHand));
                } else {
                    pStack.shrink(1);
                }
            }

            pPlayer.awardStat(Stats.ITEM_USED.get(item));
            return ItemInteractionResult.sidedSuccess(pLevel.isClientSide);
        }

        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
        this.startFuse(level, pos, igniter);
    }

    public void startFuse(Level level, BlockPos pos, LivingEntity igniter) {
        if (!level.isClientSide) {
            var primedFusionTNTEntity = new FusionTNTPrimedEntity(level, pos.getX() + 0.5F, pos.getY(),
                    pos.getZ() + 0.5F, igniter);
            level.addFreshEntity(primedFusionTNTEntity);
            level.playSound(null, primedFusionTNTEntity.getX(), primedFusionTNTEntity.getY(),
                    primedFusionTNTEntity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1, 1);
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected int getLightBlock(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1;
    }

    @Override
    protected void onExplosionHit(BlockState pState, Level pLevel, BlockPos pPos, Explosion pExplosion, BiConsumer<ItemStack, BlockPos> pDropConsumer) {
        super.onExplosionHit(pState, pLevel, pPos, pExplosion, pDropConsumer);
    }

    @Override
    public boolean dropFromExplosion(Explosion pExplosion) {
        return false;
    }
}
