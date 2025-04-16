package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.block.entity.AssemblerPartBlockEntity;
import com.mattutos.arkfuture.block.entity.CoalPowerGeneratorBlockEntity;
import com.mattutos.arkfuture.block.entity.MechanicalAssemblerBlockEntity;
import com.mattutos.arkfuture.block.entity.MechanicalTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArkFuture.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    public static final RegistryObject<BlockEntityType<CoalPowerGeneratorBlockEntity>> COAL_POWER_GENERATOR = BLOCK_ENTITIES.register("coal_power_generator",
            () -> BlockEntityType.Builder.of(CoalPowerGeneratorBlockEntity::new, BlockInit.COAL_POWER_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<MechanicalAssemblerBlockEntity>> MECHANICAL_ASSEMBLER = BLOCK_ENTITIES.register("mechanical_assembler",
            () -> BlockEntityType.Builder.of(MechanicalAssemblerBlockEntity::new, BlockInit.MECHANICAL_ASSEMBLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<AssemblerPartBlockEntity>> ASSEMBLER_PART = BLOCK_ENTITIES.register("assembler_part",
            () -> BlockEntityType.Builder.of(AssemblerPartBlockEntity::new, BlockInit.ASSEMBLER_PART.get()).build(null));

    public static final RegistryObject<BlockEntityType<MechanicalTableBlockEntity>> MECHANICAL_TABLE =
            BLOCK_ENTITIES.register("mechanical_table", () -> BlockEntityType.Builder.of(
                    MechanicalTableBlockEntity::new, BlockInit.MECHANICAL_TABLE.get()).build(null));
}
