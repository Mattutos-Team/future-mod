package com.mattutos.arkfuture.block.entity.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PrimedFusionTNT extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Integer> DATA_FUSE_ID;
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID;
    private static final int DEFAULT_FUSE_TIME = 80;
    private static final String TAG_BLOCK_STATE = "block_state";
    public static final String TAG_FUSE = "fuse";
    private static final ExplosionDamageCalculator USED_PORTAL_DAMAGE_CALCULATOR;
    @javax.annotation.Nullable
    private LivingEntity owner;
    private boolean usedPortal;


    public PrimedFusionTNT(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.blocksBuilding = true;
    }

    public PrimedFusionTNT(Level level, double x, double y, double z, @javax.annotation.Nullable LivingEntity owner) {
        this(EntityType.TNT, level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * (double) ((float) Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, (double) 0.2F, -Math.cos(d0) * 0.02);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_FUSE_ID, 80);
        pBuilder.define(DATA_BLOCK_STATE_ID, Blocks.TNT.defaultBlockState());
    }


    //USE TO EMIT EFFECTS AND SOUNDS (PARTICLES, ETC...) (DISABLED)
    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    //USE TO DEFINE WHETHER THE ENTITY IS TARGET OR NOT
    public boolean isPickable() {
        return !this.isRemoved();
    }

    //DEFINE THE GRAVITY DOWNWARD PER TICK
    protected double getDefaultGravity() {
        return 0.04;
    }


    protected void explode() {
        float f = 4.0F;
        this.level().explode(this, Explosion.getDefaultDamageSource(this.level(), this), this.usedPortal ? USED_PORTAL_DAMAGE_CALCULATOR : null, this.getX(), this.getY((double) 0.0625F), this.getZ(), 4.0F, false, Level.ExplosionInteraction.TNT);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.setFuse(pCompound.getShort("fuse"));
        if (pCompound.contains("block_state", 10)) {
            this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), pCompound.getCompound("block_state")));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public @Nullable Entity getOwner() {
        return this.owner;
    }

    public void setFuse(int life) {
        this.entityData.set(DATA_FUSE_ID, life);
    }

    public int getFuse() {
        return (Integer) this.entityData.get(DATA_FUSE_ID);
    }

    public void setBlockState(BlockState blockState) {
        this.entityData.set(DATA_BLOCK_STATE_ID, blockState);
    }

    public BlockState getBlockState() {
        return (BlockState) this.entityData.get(DATA_BLOCK_STATE_ID);
    }

    private void setUsedPortal(boolean usedPortal) {
        this.usedPortal = usedPortal;
    }

    @javax.annotation.Nullable
    public Entity changeDimension(DimensionTransition transition) {
        Entity entity = super.changeDimension(transition);
        if (entity instanceof PrimedTnt primedtnt) {
            this.setUsedPortal(true);
        }

        return entity;
    }

    static {
        DATA_FUSE_ID = SynchedEntityData.defineId(PrimedFusionTNT.class, EntityDataSerializers.INT);
        DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(PrimedFusionTNT.class, EntityDataSerializers.BLOCK_STATE);
        USED_PORTAL_DAMAGE_CALCULATOR = new ExplosionDamageCalculator() {
            public boolean shouldBlockExplode(Explosion p_353087_, BlockGetter p_353096_, BlockPos p_353092_, BlockState p_353086_, float p_353094_) {
                return !p_353086_.is(Blocks.NETHER_PORTAL) && super.shouldBlockExplode(p_353087_, p_353096_, p_353092_, p_353086_, p_353094_);
            }

            public @NotNull Optional<Float> getBlockExplosionResistance(Explosion p_353090_, BlockGetter p_353088_, BlockPos p_353091_, BlockState p_353093_, FluidState p_353095_) {
                return p_353093_.is(Blocks.NETHER_PORTAL) ? Optional.empty() : super.getBlockExplosionResistance(p_353090_, p_353088_, p_353091_, p_353093_, p_353095_);
            }
        };
    }
}
