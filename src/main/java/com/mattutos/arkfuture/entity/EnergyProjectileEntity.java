package com.mattutos.arkfuture.entity;

import com.mattutos.arkfuture.init.EntityInit;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EnergyProjectileEntity extends AbstractHurtingProjectile {
    private Vec3 origin;
    private boolean initialized = false;

    public EnergyProjectileEntity(EntityType<? extends AbstractHurtingProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public EnergyProjectileEntity(Level level, LivingEntity shooter, Vec3 vec3) {
        super(EntityInit.ENERGY_PROJECTILE.get(), shooter, vec3, level);
        this.origin = shooter.position();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        pResult.getEntity().hurt(this.getOwner().damageSources().lightningBolt(), 6.0F);
        this.discard(); // Remove a entidade após atingir algo
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (origin == null) this.discard();

            // Distância entre a posição atual e a posição inicial
            double distance = this.position().distanceTo(origin);

            // Se a distância for maior que 10 blocos, remove a entidade
            if (distance > 10) {
                this.discard();

                // Efeito de dissipação
                ((ServerLevel) level()).sendParticles(ParticleTypes.SMOKE, getX(), getY(), getZ(), 5, 0.1, 0.1, 0.1, 0.01);
                level().playSound(null, blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        } else {
            // partículas ou efeitos visuais
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.END_ROD;
    }

}
