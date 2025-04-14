package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.item.PrimedFusionTNT;
import com.mattutos.arkfuture.init.EntityInit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
}
