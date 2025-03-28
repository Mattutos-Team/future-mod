package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;

import com.mattutos.arkfuture.block.CoalPowerGeneratorBlock;
import com.mattutos.arkfuture.block.MechanicalTableBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArkFuture.MOD_ID);

    public static final RegistryObject<CoalPowerGeneratorBlock> COAL_POWER_GENERATOR = registerBlock("coal_power_generator",
            () -> new CoalPowerGeneratorBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.STONE)));

    public static final RegistryObject<MechanicalTableBlock> MECHANICAL_TABLE = registerBlock("mechanical_table",
            () -> new MechanicalTableBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.WOOD)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
