package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.block.*;
import com.mattutos.arkfuture.item.VitalEnergyCubeItem;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArkFuture.MOD_ID);

    public static final RegistryObject<CoalPowerGeneratorBlock> COAL_POWER_GENERATOR = registerBlock("coal_power_generator",
            () -> new CoalPowerGeneratorBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.STONE)));

    public static final RegistryObject<MechanicalAssemblerBlock> MECHANICAL_ASSEMBLER = registerBlock("mechanical_assembler",
            () -> new MechanicalAssemblerBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<AssemblerPartBlock> ASSEMBLER_PART = registerBlock("assembler_part",
            () -> new AssemblerPartBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final RegistryObject<MechanicalTableBlock> MECHANICAL_TABLE = registerBlock("mechanical_table",
            () -> new MechanicalTableBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> VITAL_ENERGY_CUBE = registerBlock("vital_energy_cube",
            () -> new VitalEnergyCubeBlock(BlockBehaviour.Properties.of().strength(4f).noOcclusion()),
            blockRegistryObject -> () -> new VitalEnergyCubeItem(blockRegistryObject.get(), new Item.Properties(), 100_000)
    );

    public static final RegistryObject<Block> ANCIENT_ORE_BLOCK_ITEM = registerBlock("ancient_ore_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 6f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ANCIENT_ORE_INGOT_BLOCK_ITEM = registerBlock("ancient_ore_ingot_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 6f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ANCIENT_ORE_VEIN_BLOCK_ITEM = registerBlock("ancient_ore_vein_block",
            () -> new DropExperienceBlock(
                    UniformInt.of(2, 4),
                    BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> DEEPSLATE_ANCIENT_ORE_VEIN_BLOCK_ITEM = registerBlock("deepslate_ancient_ore_vein_block",
            () -> new DropExperienceBlock(
                    UniformInt.of(2, 4),
                    BlockBehaviour.Properties.of().strength(4.5f, 3f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }

    private static <T extends Block, I extends Item> RegistryObject<T> registerBlock(String name, Supplier<T> block, Function<RegistryObject<T>, Supplier<I>> itemFactory) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, itemFactory.apply(toReturn));
        return toReturn;
    }

    private static <T extends Item> void registerBlockItem(String name, Supplier<T> item) {
        ItemInit.ITEMS.register(name, item);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
