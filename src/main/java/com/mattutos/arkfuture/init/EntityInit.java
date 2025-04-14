package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.block.entity.FusionTNTPrimedEntity;
import com.mattutos.arkfuture.entity.EnergyProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ArkFuture.MOD_ID);

    public static final RegistryObject<EntityType<EnergyProjectileEntity>> ENERGY_PROJECTILE =
            ENTITIES.register("energy_projectile", () ->
                    EntityType.Builder.<EnergyProjectileEntity>of(EnergyProjectileEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("energy_projectile"));


    public static final RegistryObject<EntityType<FusionTNTPrimedEntity>> FUSION_TNT_PRIMED =
            ENTITIES.register("fusion_tnt_primed", () ->
                    EntityType.Builder.<FusionTNTPrimedEntity>of(FusionTNTPrimedEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("fusion_tnt_primed"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
