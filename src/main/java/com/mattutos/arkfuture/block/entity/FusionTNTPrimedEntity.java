package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.block.entity.item.PrimedFusionTNT;
import com.mattutos.arkfuture.init.BlockInit;
import com.mattutos.arkfuture.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FusionTNTPrimedEntity extends PrimedFusionTNT {

    private LivingEntity placedBy;

    public FusionTNTPrimedEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.blocksBuilding = true;
    }

    public FusionTNTPrimedEntity(Level level, double x, double y, double z,
                                 @Nullable LivingEntity igniter) {
        super(EntityInit.FUSION_TNT_PRIMED.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * ((float) Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.placedBy = igniter;
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return this.placedBy;
    }


    @Override
    public void tick() {

        //HANDLE WITH MINECRAFT FLUIDS
        this.updateInWaterStateAndDoFluidPushing();

        //PREVIOUS POSITION
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        //HANDLE MOVEMENTS WHEN IS ON THE GROUND OR PLACE ON THE AIR
        this.setDeltaMovement(this.getDeltaMovement().subtract(0, 0.03999999910593033D, 0));
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(
                this.getDeltaMovement().multiply(0.9800000190734863D, 0.9800000190734863D, 0.9800000190734863D));

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.699999988079071D, 0.699999988079071D, -0.5D));
        }


        if (this.isInWater() && !this.level().isClientSide()) {
            ItemStack tntStack = new ItemStack(BlockInit.FUSION_TNT_BLOCK.get());

            final ItemEntity item = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(),
                    tntStack);

            item.setDeltaMovement(this.getDeltaMovement());
            item.xo = this.xo;
            item.yo = this.yo;
            item.zo = this.zo;

            this.level().addFreshEntity(item);
            this.discard();
        }

        if (this.getFuse() <= 0) {
            this.discard();

            if (!this.level().isClientSide) {
                this.explode();
            }
        } else {
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D,
                    0.0D);
        }
        this.setFuse(this.getFuse() - 1);
    }

    @Override
    protected void explode() {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.BLOCKS, 4.0F,
                (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 32.9F);

        if (this.isInWater()) {
            return;
        }

        final Explosion ex = new Explosion(this.level(), this, this.getX(), this.getY(), this.getZ(),
                0.2f, false, Explosion.BlockInteraction.DESTROY_WITH_DECAY
        );

        final AABB area = new AABB(this.getX() - 1.5, this.getY() - 1.5f, this.getZ() - 1.5,
                this.getX() + 1.5, this.getY() + 1.5, this.getZ() + 1.5);

        final List<Entity> list = this.level().getEntities(this, area);

        ForgeEventFactory.onExplosionDetonate(this.level(), ex, list, 0.2f * 2d);

        for (Entity e : list) {
            e.hurt(level().damageSources().explosion(ex), 6);
        }

        this.setPos(this.getX(), this.getY() - 0.25, this.getZ());

        for (int x = (int) (this.getX() - 2); x <= this.getX() + 2; x++) {
            for (int y = (int) (this.getY() - 2); y <= this.getY() + 2; y++) {
                for (int z = (int) (this.getZ() - 2); z <= this.getZ() + 2; z++) {
                    final BlockPos point = new BlockPos(x, y, z);
                    final BlockState state = this.level().getBlockState(point);
                    final Block block = state.getBlock();

                    if (!state.isAir()) {
                        float strength = (float) (2.3f
                                - ((x + 0.5f - this.getX()) * (x + 0.5f - this.getX())
                                + (y + 0.5f - this.getY()) * (y + 0.5f - this.getY())
                                + (z + 0.5f - this.getZ()) * (z + 0.5f - this.getZ())));

                        final float fluidResistance = !state.getFluidState().isEmpty()
                                ? state.getFluidState().getExplosionResistance()
                                : 0f;
                        final float resistance = Math
                                .max(block.getExplosionResistance(state, this.level(), point, ex), fluidResistance);
                        strength -= (resistance + 0.3F) * 0.11f;

                        if (strength > 0.01 && !state.isAir()) {
                            if (state.canDropFromExplosion(this.level(), point, ex)) {
                                Block.dropResources(state, this.level(), point, this.level().getBlockEntity(point));
                            }

                            level().setBlock(point, Blocks.AIR.defaultBlockState(), 3);
                            state.onBlockExploded(this.level(), point, ex);
                        }
                    }
                }
            }
        }

    }
}
