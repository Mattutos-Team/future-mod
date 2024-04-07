package com.mattutos.future.init;

import com.mattutos.future.FutureMod;
import com.mattutos.future.block.CoalEnergyGeneratorBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FutureMod.MOD_ID);

    public static final RegistryObject<Block> OLD_ORE = registerBlock("old_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(3f).requiresCorrectToolForDrops()));

    public static final RegistryObject<CoalEnergyGeneratorBlock> COAL_ENERGY_GENERATOR =
            registerBlock("coal_energy_generator", () -> new CoalEnergyGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
