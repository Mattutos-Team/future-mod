package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.item.PrimedFusionTNT;
import com.mattutos.arkfuture.init.BlockInit;
import com.mattutos.arkfuture.init.EntityInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
}
