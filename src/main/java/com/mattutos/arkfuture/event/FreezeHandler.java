package com.mattutos.arkfuture.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FreezeHandler {
    private static final Map<UUID, Long> frozenEntities = new HashMap<>();

    public static void freezeEntity(LivingEntity entity, int durationTicks) {
        frozenEntities.put(entity.getUUID(), System.currentTimeMillis() + (durationTicks * 50L)); // 1 tick = 50ms
    }

    public static boolean isFrozen(LivingEntity entity) {
        return frozenEntities.containsKey(entity.getUUID()) && System.currentTimeMillis() < frozenEntities.get(entity.getUUID());
    }

    public static void tickEntity(LivingEntity entity) {
        if (isFrozen(entity)) {
            entity.setDeltaMovement(Vec3.ZERO);
            entity.hurtMarked = true;

            // Efeito visual opcional
            entity.level().addParticle(ParticleTypes.ELECTRIC_SPARK, entity.getX(), entity.getY(), entity.getZ(), 0.0, 0.0, 0.0);
        } else {
            frozenEntities.remove(entity.getUUID());
        }
    }
}
