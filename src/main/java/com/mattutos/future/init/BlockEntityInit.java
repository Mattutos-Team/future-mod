package com.mattutos.future.init;

import com.mattutos.future.FutureMod;
import com.mattutos.future.blockentity.CoalEnergyGeneratorEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FutureMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<CoalEnergyGeneratorEntity>> COAL_ENERGY_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES
            .register("coal_energy_generator",
                    () -> BlockEntityType.Builder.of(CoalEnergyGeneratorEntity::new, BlockInit.COAL_ENERGY_GENERATOR.get())
                            .build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
