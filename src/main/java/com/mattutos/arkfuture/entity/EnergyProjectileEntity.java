package com.mattutos.arkfuture.entity;

import com.mattutos.arkfuture.event.FreezeHandler;
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

        if (pResult.getEntity() instanceof LivingEntity living) {
            pResult.getEntity().hurt(this.getOwner().damageSources().lightningBolt(), 0.5F);
            FreezeHandler.freezeEntity(living, 40); // 40 ticks = 2 segundos
            // Opcional: som ou partículas adicionais
//            living.level().playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.5F, 0.5F);
        }

        this.disolve(); // Remove a entidade após atingir algo
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.disolve();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (origin == null) this.discard();

            // Distância entre a posição atual e a posição inicial
            double distance = this.position().distanceTo(origin);

            // Se a distância for maior que 10 blocos, remove a entidade
            if (distance > 10) this.disolve();
        } else {
            // partículas ou efeitos visuais
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.END_ROD;
    }

    private void disolve() {
        // Lógica para dissipar a entidade
        this.discard();

        // Efeito de dissipação
        ((ServerLevel) level()).sendParticles(ParticleTypes.SMOKE, getX(), getY(), getZ(), 5, 0.1, 0.1, 0.1, 0.01);
        level().playSound(null, blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.2F, 1.0F);
    }

}
